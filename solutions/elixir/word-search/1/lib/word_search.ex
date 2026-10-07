defmodule WordSearch do
  @directions [
    {0, 1},
    {0, -1},
    {1, 0},
    {-1, 0},
    {1, 1},
    {-1, -1},
    {1, -1},
    {-1, 1}
  ]

  defmodule Location do
    defstruct [:from, :to]

    @type t :: %Location{
            from: %{row: integer, column: integer},
            to: %{row: integer, column: integer}
          }
  end

  @doc """
  Find the start and end positions of words in a grid of letters.
  Row and column positions are 1 indexed.
  """
  @spec search(grid :: String.t(), words :: [String.t()]) :: %{String.t() => nil | Location.t()}
  def search(grid, words) do
    rows = grid |> String.split("\n", trim: true) |> Enum.map(&String.graphemes/1)

    Map.new(words, fn word ->
      {word, find_word(rows, word)}
    end)
  end

  defp find_word(_rows, ""), do: nil

  defp find_word(rows, word) do
    characters = String.graphemes(word)
    height = length(rows)
    width = rows |> List.first([]) |> length()

    candidates =
      for row <- 0..(height - 1),
          column <- 0..(width - 1),
          direction <- @directions,
          matches?(rows, characters, row, column, direction),
          do: location(row, column, characters, direction)

    List.first(candidates)
  end

  defp matches?(rows, characters, row, column, {row_step, column_step}) do
    characters
    |> Enum.with_index()
    |> Enum.all?(fn {character, index} ->
      cell_at(rows, row + row_step * index, column + column_step * index) == character
    end)
  end

  defp cell_at(rows, row, column) when row >= 0 and column >= 0 do
    rows |> Enum.at(row, []) |> Enum.at(column)
  end

  defp cell_at(_rows, _row, _column), do: nil

  defp location(row, column, characters, {row_step, column_step}) do
    last_index = length(characters) - 1

    %Location{
      from: %{row: row + 1, column: column + 1},
      to: %{
        row: row + row_step * last_index + 1,
        column: column + column_step * last_index + 1
      }
    }
  end
end
