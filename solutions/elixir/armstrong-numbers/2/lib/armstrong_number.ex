defmodule ArmstrongNumber do
  @moduledoc """
  Provides a way to validate whether or not a number is an Armstrong number
  """

  @spec valid?(integer) :: boolean
  def valid?(number) do
    digits = Integer.digits(number)
    power = length(digits)

    digits
    |> Enum.map(&Integer.pow(&1, power))
    |> Enum.sum()
    |> then(&(&1 == number))
  end
end
