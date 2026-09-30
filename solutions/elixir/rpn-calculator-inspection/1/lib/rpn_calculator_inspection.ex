defmodule RPNCalculatorInspection do
  def start_reliability_check(calculator, input) do
    pid = spawn_link(fn -> calculator.(input) end)
    %{input: input, pid: pid}
  end

  def await_reliability_check_result(%{pid: pid, input: input}, results) do
    result =
      receive do
        {:EXIT, ^pid, :normal} -> :ok
        {:EXIT, ^pid, _reason} -> :error
      after
        100 -> :timeout
      end

    Map.put(results, input, result)
  end

  def reliability_check(calculator, inputs) do
    original_trap_exit = Process.flag(:trap_exit, true)

    try do
      checks = Enum.map(inputs, &start_reliability_check(calculator, &1))
      Enum.reduce(checks, %{}, &await_reliability_check_result(&1, &2))
    after
      Process.flag(:trap_exit, original_trap_exit)
    end
  end

  def correctness_check(calculator, inputs) do
    inputs
    |> Enum.map(&Task.async(fn -> calculator.(&1) end))
    |> Enum.map(&Task.await(&1, 100))
  end
end
