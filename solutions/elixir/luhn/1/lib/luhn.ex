defmodule Luhn do
  @doc """
  Checks if the given number is valid via the luhn formula
  """
  @spec valid?(String.t()) :: boolean
  def valid?(number) do
    match = number =~ ~r/^\d[\d\s]{1,}$/
    match && luhn_sum(number)
  end

  defp luhn_sum(number) do
    Regex.scan(~r/\d/, number)
    |> Enum.map(fn [c] -> String.to_integer(c) end)
    |> Enum.reverse
    |> Enum.with_index(&double_every_second_digit/2)
    |> Enum.sum
    |> Integer.mod(10)
    |> then(&(&1 == 0))
  end
  
  defp double_every_second_digit(n, idx) when rem(idx, 2) == 0, do: n
  
  defp double_every_second_digit(x, _) do
    case x * 2 do
      n when n > 9 -> n - 9
      n -> n
    end
  end

end
