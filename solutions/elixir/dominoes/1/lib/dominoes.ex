defmodule Dominoes do
  @type domino :: {1..6, 1..6}

  @doc """
  chain?/1 takes a list of domino stones and returns boolean indicating if it's
  possible to make a full chain
  """
  @spec chain?(dominoes :: [domino]) :: boolean
  def chain?([]), do: true

  def chain?([first | rest]) do
    chain_from(rest, elem(first, 1), elem(first, 0))
  end

  defp chain_from([], current, target), do: current == target

  defp chain_from(dominoes, current, target) do
    dominoes
    |> Enum.with_index()
    |> Enum.filter(fn {domino, _index} -> connects?(domino, current) end)
    |> Enum.any?(fn {domino, index} ->
      remaining = List.delete_at(dominoes, index)

      case domino do
        {^current, next} -> chain_from(remaining, next, target)
        {next, ^current} -> chain_from(remaining, next, target)
        _ -> false
      end
    end)
  end

  defp connects?({left, right}, value), do: left == value or right == value
end
