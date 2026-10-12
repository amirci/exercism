defmodule Clock do
  @type t() :: %__MODULE__{hour: integer(), minute: integer()}
  defstruct hour: 0, minute: 0

  @minutes_per_day 24 * 60

  @spec new(integer(), integer()) :: t()
  def new(hour, minute) do
    total_minutes = Integer.mod(hour * 60 + minute, @minutes_per_day)
    %Clock{hour: div(total_minutes, 60), minute: rem(total_minutes, 60)}
  end

  @spec add(t(), integer()) :: t()
  def add(%Clock{hour: hour, minute: minute}, add_minutes) do
    new(hour, minute + add_minutes)
  end
end

defimpl String.Chars, for: Clock do
  def to_string(%Clock{hour: hour, minute: minute}) do
    :io_lib.format("~2..0B:~2..0B", [hour, minute])
    |> IO.iodata_to_binary()
  end
end
