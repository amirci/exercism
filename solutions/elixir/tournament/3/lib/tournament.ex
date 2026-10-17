defmodule Tournament do
  defmodule TeamStat do
    @moduledoc false
    defstruct win: 0, loss: 0, draw: 0

    def matches_played(%__MODULE__{} = stats), do: stats.win + stats.loss + stats.draw
    def points(%__MODULE__{} = stats), do: stats.win * 3 + stats.draw
  end

  @header "Team                           | MP |  W |  D |  L |  P"

  @doc """
  Given `input` lines representing two teams and whether the first of them won,
  lost, or reached a draw, separated by semicolons, calculate the statistics
  for each team's number of games played, won, drawn, lost, and total points
  for the season, and return a nicely-formatted string table.

  A win earns a team 3 points, a draw earns 1 point, and a loss earns nothing.

  Order the outcome by most total points for the season, and settle ties by
  listing the teams in alphabetical order.
  """
  @spec tally(input :: list(String.t())) :: String.t()
  def tally(input) do
    input
    |> Enum.reduce(%{}, &record_line/2)
    |> Enum.sort_by(fn {team, stats} -> {-TeamStat.points(stats), team} end)
    |> format_table()
  end

  defp record_line(line, stats) do
    String.split(line, ";")
    |> parse_updates()
    |> Enum.reduce(stats, &update_team/2)
  end

  defp parse_updates([fst, snd, "win"]), do: [{fst, %{win: 1}}, {snd, %{loss: 1}}]
  defp parse_updates([fst, snd, "loss"]), do: [{fst, %{loss: 1}}, {snd, %{win: 1}}]
  defp parse_updates([fst, snd, "draw"]), do: [{fst, %{draw: 1}}, {snd, %{draw: 1}}]
  defp parse_updates(_), do: []

  defp update_team({team, result}, stats) do
    adding_scores = fn _, existing, new_value -> existing + new_value end

    merge_scores_by_adding = fn current -> Map.merge(current, result, adding_scores) end

    Map.update(stats, team, struct(TeamStat, result), merge_scores_by_adding)
  end

  defp format_table(stats) do
    stats
    |> Enum.map(&format_row/1)
    |> prepend_header()
    |> Enum.join("\n")
  end

  defp prepend_header(rows), do: [@header | rows]

  defp format_row({team, %TeamStat{} = stats}) do
    [
      String.pad_trailing(team, 30),
      pad(TeamStat.matches_played(stats)),
      pad(stats.win),
      pad(stats.draw),
      pad(stats.loss),
      pad(TeamStat.points(stats))
    ]
    |> Enum.join(" | ")
  end

  defp pad(number), do: number |> Integer.to_string() |> String.pad_leading(2)
end
