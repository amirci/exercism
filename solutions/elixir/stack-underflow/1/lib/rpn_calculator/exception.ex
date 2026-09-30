defmodule RPNCalculator.Exception do
  defmodule DivisionByZeroError do
    defexception message: "division by zero occurred"
  end

  defmodule StackUnderflowError do
    defexception message: "stack underflow occurred"

    @impl true
    def exception([]), do: %__MODULE__{}

    @impl true
    def exception(context) when is_binary(context) do
      %__MODULE__{message: "stack underflow occurred, context: #{context}"}
    end
  end

  def divide([divisor, dividend | _]) do
    if divisor == 0 do
      raise DivisionByZeroError
    end

    dividend / divisor
  end

  def divide(_stack) do
    raise StackUnderflowError, "when dividing"
  end
end
