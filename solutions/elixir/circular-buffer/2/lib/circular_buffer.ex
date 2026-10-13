defmodule CircularBuffer do
  @moduledoc """
  An API to a stateful process that fills and empties a circular buffer.
  """

  use GenServer

  alias CircularBuffer.Buffer

  @doc """
  Create a new buffer of a given capacity.
  """
  @spec new(capacity :: integer) :: {:ok, pid}
  def new(capacity), do: GenServer.start_link(__MODULE__, capacity)

  @doc """
  Read the oldest entry in the buffer, failing if it is empty.
  """
  @spec read(buffer :: pid) :: {:ok, any} | {:error, atom}
  def read(buffer), do: GenServer.call(buffer, :read)

  @doc """
  Write a new item in the buffer, failing if it is full.
  """
  @spec write(buffer :: pid, item :: any) :: :ok | {:error, atom}
  def write(buffer, item), do: GenServer.call(buffer, {:write, item})

  @doc """
  Write an item, overwriting the oldest entry if the buffer is full.
  """
  @spec overwrite(buffer :: pid, item :: any) :: :ok
  def overwrite(buffer, item), do: GenServer.call(buffer, {:overwrite, item})

  @doc """
  Clear the buffer.
  """
  @spec clear(buffer :: pid) :: :ok
  def clear(buffer), do: GenServer.call(buffer, :clear)

  @impl GenServer
  def init(capacity), do: {:ok, Buffer.new(capacity)}

  @impl GenServer
  def handle_call(request, _from, state) do
    {reply, new_state} = dispatch(request, state)
    {:reply, reply, new_state}
  end

  defp dispatch(:read, state), do: Buffer.read(state)
  defp dispatch({:write, item}, state), do: Buffer.write(state, item)
  defp dispatch({:overwrite, item}, state), do: Buffer.overwrite(state, item)
  defp dispatch(:clear, state), do: Buffer.clear(state)
end

defmodule CircularBuffer.Buffer do
  @moduledoc false

  @type t :: %__MODULE__{capacity: integer(), queue: term()}
  @type transition(reply) :: {reply, t()}

  defstruct [:capacity, queue: :queue.new()]

  @spec new(integer()) :: t()
  def new(capacity), do: %__MODULE__{capacity: capacity}

  @spec read(t()) :: transition({:ok, term()} | {:error, :empty})
  def read(%__MODULE__{queue: queue} = state) do
    case :queue.out(queue) do
      {:empty, _queue} -> {{:error, :empty}, state}
      {{:value, item}, queue} -> {{:ok, item}, %{state | queue: queue}}
    end
  end

  @spec write(t(), term()) :: transition(:ok | {:error, :full})
  def write(state, item) do
    if full?(state) do
      {{:error, :full}, state}
    else
      {:ok, enqueue(state, item)}
    end
  end

  @spec overwrite(t(), term()) :: transition(:ok)
  def overwrite(state, item) do
    state = if full?(state), do: discard_oldest(state), else: state
    {:ok, enqueue(state, item)}
  end

  @spec clear(t()) :: transition(:ok)
  def clear(state), do: {:ok, %{state | queue: :queue.new()}}

  defp full?(state), do: :queue.len(state.queue) == state.capacity

  defp enqueue(state, item), do: %{state | queue: :queue.in(item, state.queue)}

  defp discard_oldest(state) do
    {{:value, _item}, queue} = :queue.out(state.queue)
    %{state | queue: queue}
  end
end
