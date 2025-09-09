defmodule Triangle do
  @type kind :: :equilateral | :isosceles | :scalene

  @doc """
  Return the kind of triangle of a triangle with 'a', 'b' and 'c' as lengths.
  """
  @spec kind(number, number, number) :: {:ok, kind} | {:error, String.t()}
  def kind(a, b, c) do
    [a, b, c] = sides = Enum.sort([a, b, c])
    cond do
      Enum.find(sides, &(&1 <= 0)) -> {:error, "all side lengths must be positive"}
      a + b < c -> {:error, "side lengths violate triangle inequality"}
      true -> {:ok, the_kind(sides)}
    end
  end

  defp the_kind(sides) do
    kinds = [:equilateral, :isosceles, :scalene]
    sides 
    |> Enum.dedup 
    |> Enum.count
    |> then(&(Enum.at(kinds, &1 - 1)))
    
  end
end
