defmodule Tournament do
  @header "Team                           | MP |  W |  D |  L |  P"
  @empty_stats %{mp: 0, w: 0, d: 0, l: 0, p: 0}

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
    |> Enum.sort_by(fn {team, %{p: points}} -> {-points, team} end)
    |> format_table()
  end

  defp record_line(line, stats) do
    case String.split(line, ";") do
      [first_team, second_team, result] -> record_match(stats, first_team, second_team, result)
      _ -> stats
    end
  end

  defp record_match(stats, first_team, second_team, result) do
    case result do
      "win" -> update_match(stats, first_team, %{w: 1, p: 3}, second_team, %{l: 1})
      "loss" -> update_match(stats, first_team, %{l: 1}, second_team, %{w: 1, p: 3})
      "draw" -> update_match(stats, first_team, %{d: 1, p: 1}, second_team, %{d: 1, p: 1})
      _ -> stats
    end
  end

  defp update_match(stats, first_team, first_result, second_team, second_result) do
    stats
    |> update_team(first_team, first_result)
    |> update_team(second_team, second_result)
  end

  defp update_team(stats, team, result) do
    increments = Map.put(result, :mp, 1)
    add_values = fn _key, current_value, result_value -> current_value + result_value end
    merge_existing = fn current -> Map.merge(current, increments, add_values) end
    initial_stats = Map.merge(@empty_stats, increments)

    Map.update(stats, team, initial_stats, merge_existing)
  end

  defp format_table(stats) do
    stats
    |> Enum.map(&format_row/1)
    |> prepend_header()
    |> Enum.join("\n")
  end

  defp prepend_header(rows), do: [@header | rows]

  defp format_row({team, %{mp: mp, w: wins, d: draws, l: losses, p: points}}) do
    [
      String.pad_trailing(team, 30),
      pad(mp),
      pad(wins),
      pad(draws),
      pad(losses),
      pad(points)
    ]
    |> Enum.join(" | ")
  end

  defp pad(number), do: number |> Integer.to_string() |> String.pad_leading(2)
end
