defmodule Alphametics do
  @type puzzle :: binary
  @type solution :: %{required(?A..?Z) => 0..9}

  @doc """
  Takes an alphametics puzzle and returns a solution where every letter
  replaced by its number will make a valid equation. Returns `nil` when
  there is no valid solution to the given puzzle.

  ## Examples

    iex> Alphametics.solve("I + BB == ILL")
    %{?I => 1, ?B => 9, ?L => 0}

    iex> Alphametics.solve("A == B")
    nil
  """
  @spec solve(puzzle) :: solution | nil
  def solve(puzzle) do
    {addends, result} = parse_puzzle(puzzle)
    leading = leading_letters(addends ++ [result])
    columns = columns(addends, result)

    case solve_columns(columns, 0, %{}, MapSet.new(), leading) do
      nil -> nil
      solution -> solution
    end
  end

  defp parse_puzzle(puzzle) do
    [left, result] = String.split(puzzle, " == ", parts: 2)
    addends = left |> String.split(" + ") |> Enum.map(&String.to_charlist/1)
    {addends, String.to_charlist(result)}
  end

  defp leading_letters(words) do
    words
    |> Enum.filter(&(length(&1) > 1))
    |> Enum.map(&hd/1)
    |> MapSet.new()
  end

  defp columns(addends, result) do
    reversed_addends = Enum.map(addends, &Enum.reverse/1)
    reversed_result = Enum.reverse(result)
    column_count = max(length(reversed_result), Enum.max(Enum.map(reversed_addends, &length/1)))

    for index <- 0..(column_count - 1) do
      addend_letters =
        reversed_addends
        |> Enum.flat_map(fn word ->
          case Enum.at(word, index) do
            nil -> []
            letter -> [letter]
          end
        end)

      {addend_letters, Enum.at(reversed_result, index)}
    end
  end

  defp solve_columns([], 0, assignment, _used, _leading), do: assignment
  defp solve_columns([], _carry, _assignment, _used, _leading), do: nil

  defp solve_columns([{addend_letters, result_letter} | rest], carry, assignment, used, leading) do
    if is_nil(result_letter) do
      nil
    else
      letters = Enum.uniq(addend_letters ++ [result_letter])
      unassigned = Enum.reject(letters, &Map.has_key?(assignment, &1))

      assign_letters(unassigned, assignment, used, leading)
      |> Enum.find_value(fn {next_assignment, next_used} ->
        total = Enum.sum(Enum.map(addend_letters, &Map.fetch!(next_assignment, &1))) + carry
        result_digit = Map.fetch!(next_assignment, result_letter)

        if rem(total, 10) == result_digit do
          solve_columns(rest, div(total, 10), next_assignment, next_used, leading)
        end
      end)
    end
  end

  defp assign_letters([], assignment, used, _leading), do: [{assignment, used}]

  defp assign_letters([letter | rest], assignment, used, leading) do
    for digit <- 0..9,
        not MapSet.member?(used, digit),
        not (digit == 0 and MapSet.member?(leading, letter)),
        {next_assignment, next_used} <-
          assign_letters(
            rest,
            Map.put(assignment, letter, digit),
            MapSet.put(used, digit),
            leading
          ) do
      {next_assignment, next_used}
    end
  end
end
