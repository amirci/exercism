defmodule Darts do
  @type position :: {number, number}

  @doc """
  Calculate the score of a single dart hitting a target
  """
  @spec score(position) :: integer
  def score({x, y}) do
    distance_squared = x * x + y * y

    cond do
      distance_squared <= 1 -> 10
      distance_squared <= 25 -> 5
      distance_squared <= 100 -> 1
      true -> 0
    end
  end
end
