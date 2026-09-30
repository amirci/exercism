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

  def divide([0, _]), do: raise(DivisionByZeroError)
  def divide([divisor, dividend]), do: dividend / divisor
  def divide(_), do: raise(StackUnderflowError, "when dividing")
end
