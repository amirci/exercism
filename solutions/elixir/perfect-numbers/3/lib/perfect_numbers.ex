defmodule PerfectNumbers do
  @doc """
  Determine the aliquot sum of the given `number`, by summing all the factors
  of `number`, aside from `number` itself.

  Based on this sum, classify the number as:

  :perfect if the aliquot sum is equal to `number`
  :abundant if the aliquot sum is greater than `number`
  :deficient if the aliquot sum is less than `number`
  """
  @spec classify(number :: integer) :: {:ok, atom} | {:error, String.t()}
  def classify(number) when number <= 0 do
    {:error, "Classification is only possible for natural numbers."}
  end

  def classify(1), do: {:ok, :deficient}

  def classify(number) do
    aliquot_sum = aliquot_sum(number)

    category =
      cond do
        aliquot_sum == number -> :perfect
        aliquot_sum > number -> :abundant
        true -> :deficient
      end

    {:ok, category}
  end

  defp aliquot_sum(number) do
    number
    |> square_root_range()
    |> Enum.reduce(0, fn divisor, sum -> add_divisors(divisor, number, sum) end)
  end

  defp square_root_range(number), do: 1..trunc(:math.sqrt(number))

  defp add_divisors(divisor, number, sum) do
    if rem(number, divisor) == 0 do
      [divisor, div(number, divisor)]
      |> Enum.uniq()
      |> Enum.reject(&(&1 == number))
      |> Enum.sum()
      |> then(&(&1 + sum))
    else
      sum
    end
  end
end
