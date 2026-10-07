defmodule Rectangles do
  @doc """
  Count the number of ASCII rectangles.
  """
  @spec count(input :: String.t()) :: integer
  def count(input) do
    rows = input |> String.split("\n", trim: true) |> Enum.map(&String.graphemes/1)
    count_rectangles(rows)
  end

  defp count_rectangles([]), do: 0

  defp count_rectangles([first | _] = rows)
       when length(rows) < 2 or length(first) < 2,
       do: 0

  defp count_rectangles(rows) do
    height = length(rows)
    width = rows |> List.first() |> length()

    for top <- 0..(height - 2),
        bottom <- (top + 1)..(height - 1),
        left <- 0..(width - 2),
        right <- (left + 1)..(width - 1),
        rectangle?(rows, top, bottom, left, right),
        reduce: 0 do
      count -> count + 1
    end
  end

  defp rectangle?(rows, top, bottom, left, right) do
    top_row = Enum.at(rows, top)
    bottom_row = Enum.at(rows, bottom)

    Enum.at(top_row, left) == "+" and
      Enum.at(top_row, right) == "+" and
      Enum.at(bottom_row, left) == "+" and
      Enum.at(bottom_row, right) == "+" and
      horizontal_side?(top_row, left, right) and
      horizontal_side?(bottom_row, left, right) and
      vertical_side?(rows, top, bottom, left) and
      vertical_side?(rows, top, bottom, right)
  end

  defp horizontal_side?(row, left, right) do
    row
    |> Enum.slice(left, right - left + 1)
    |> Enum.all?(&(&1 in ["+", "-"]))
  end

  defp vertical_side?(rows, top, bottom, column) do
    rows
    |> Enum.slice(top, bottom - top + 1)
    |> Enum.all?(fn row -> Enum.at(row, column) in ["+", "|"] end)
  end
end
