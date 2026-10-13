defmodule CircularBuffer do
  @moduledoc """
  An API to a stateful process that fills and empties a circular buffer.
  """

  use GenServer

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
  def init(capacity), do: {:ok, %{capacity: capacity, queue: :queue.new()}}

  @impl GenServer
  def handle_call(:read, _from, state) do
    case :queue.out(state.queue) do
      {:empty, _queue} -> {:reply, {:error, :empty}, state}
      {{:value, item}, queue} -> {:reply, {:ok, item}, %{state | queue: queue}}
    end
  end

  @impl GenServer
  def handle_call({:write, item}, _from, state) do
    if full?(state) do
      {:reply, {:error, :full}, state}
    else
      {:reply, :ok, enqueue(state, item)}
    end
  end

  @impl GenServer
  def handle_call({:overwrite, item}, _from, state) do
    state = if full?(state), do: discard_oldest(state), else: state
    {:reply, :ok, enqueue(state, item)}
  end

  @impl GenServer
  def handle_call(:clear, _from, state), do: {:reply, :ok, %{state | queue: :queue.new()}}

  defp full?(state), do: :queue.len(state.queue) == state.capacity

  defp enqueue(state, item), do: %{state | queue: :queue.in(item, state.queue)}

  defp discard_oldest(state) do
    {{:value, _item}, queue} = :queue.out(state.queue)
    %{state | queue: queue}
  end
end
