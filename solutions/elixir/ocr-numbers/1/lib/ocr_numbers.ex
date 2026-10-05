defmodule OcrNumbers do
  @digits %{
    Enum.join([" _ ", "| |", "|_|", "   "]) => "0",
    Enum.join(["   ", "  |", "  |", "   "]) => "1",
    Enum.join([" _ ", " _|", "|_ ", "   "]) => "2",
    Enum.join([" _ ", " _|", " _|", "   "]) => "3",
    Enum.join(["   ", "|_|", "  |", "   "]) => "4",
    Enum.join([" _ ", "|_ ", " _|", "   "]) => "5",
    Enum.join([" _ ", "|_ ", "|_|", "   "]) => "6",
    Enum.join([" _ ", "  |", "  |", "   "]) => "7",
    Enum.join([" _ ", "|_|", "|_|", "   "]) => "8",
    Enum.join([" _ ", "|_|", " _|", "   "]) => "9"
  }

  @doc """
  Given a 3 x 4 grid of pipes, underscores, and spaces, determine which number is represented, or
  whether it is garbled.
  """
  @spec convert([String.t()]) :: {:ok, String.t()} | {:error, String.t()}
  def convert(input) do
    cond do
      rem(length(input), 4) != 0 -> {:error, "invalid line count"}
      input == [] -> {:ok, ""}
      invalid_widths?(input) -> {:error, "invalid column count"}
      true -> {:ok, decode(input)}
    end
  end

  defp invalid_widths?(input) do
    widths = Enum.map(input, &String.length/1)
    Enum.any?(widths, &rem(&1, 3) != 0) or Enum.any?(widths, &(&1 != hd(widths)))
  end

  defp decode(input) do
    input
    |> Enum.chunk_every(4)
    |> Enum.map_join(",", &decode_group/1)
  end

  defp decode_group([first | _] = rows) do
    first
    |> String.length()
    |> div(3)
    |> then(fn cells ->
      0..(cells - 1)
      |> Enum.map_join(fn cell -> decode_cell(rows, cell) end)
    end)
  end

  defp decode_cell(rows, cell) do
    rows
    |> Enum.map_join(&String.slice(&1, cell * 3, 3))
    |> then(&Map.get(@digits, &1, "?"))
  end
end
