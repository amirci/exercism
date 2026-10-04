defmodule ResistorColor do
  @doc """
  Return the value of a color band
  """
  @spec code(atom) :: integer()
  def code(color), do: Enum.find_index(colors(), &(&1 == color))

  defp colors do
    [:black, :brown, :red, :orange, :yellow, :green, :blue, :violet, :grey, :white]
  end
end
