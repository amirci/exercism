defmodule AllYourBase do
  @doc """
  Given a number in input base, represented as a sequence of digits, converts it to output base,
  or returns an error tuple if either of the bases are less than 2
  """

  @spec convert(list, integer, integer) :: {:ok, list} | {:error, String.t()}
  def convert(_, input_base, _) when input_base < 2,
    do: {:error, "input base must be >= 2"}

  def convert(_, _, output_base) when output_base < 2,
    do: {:error, "output base must be >= 2"}

  def convert(digits, input_base, output_base) do
    if Enum.any?(digits, &(&1 < 0 or &1 >= input_base)) do
      {:error, "all digits must be >= 0 and < input base"}
    else
      digits
      |> to_decimal(input_base)
      |> from_decimal(output_base)
      |> then(&{:ok, &1})
    end
  end

  defp to_decimal(digits, base), do: Enum.reduce(digits, 0, &(&2 * base + &1))

  defp from_decimal(0, _base), do: [0]
  defp from_decimal(decimal, base), do: from_decimal(decimal, base, [])

  defp from_decimal(0, _base, digits), do: digits

  defp from_decimal(decimal, base, digits) do
    from_decimal(div(decimal, base), base, [rem(decimal, base) | digits])
  end
end
