defmodule BookStore do
  @typedoc "A book is represented by its number in the 5-book series"
  @type book :: 1 | 2 | 3 | 4 | 5
  @type basket :: [book()]

  @price_for_sets %{
    1 => 800,
    2 => 1520,
    3 => 2160,
    4 => 2560,
    5 => 3000
  }

  @doc """
  Calculate lowest price (in cents) for a shopping basket containing books.
  """
  @spec total(basket :: basket()) :: integer()
  def total(basket) do
    basket
    |> sorted_frequencies()
    |> possible_group_counts()
    |> recombine_sets_of_three_and_five()
    |> Enum.reduce(0, &add_group_price/2)
  end

  defp sorted_frequencies(basket) do
    basket
    |> Enum.frequencies()
    |> Map.values()
    |> Enum.sort()
  end

  defp possible_group_counts(counts) do
    counts
    |> Stream.unfold(&next_group/1)
    |> Enum.reduce(%{}, fn {size, count}, groups -> Map.update(groups, size, count, &(&1 + count)) end)
  end

  defp next_group([]), do: nil

  defp next_group([smallest | rest] = current) do
    next =
      rest
      |> Enum.map(&(&1 - smallest))
      |> Enum.reject(&(&1 == 0))

    {{length(current), smallest}, next}
  end

  defp recombine_sets_of_three_and_five(%{5 => sets_of_five, 3 => sets_of_three} = counts) do
    recombinations = min(sets_of_five, sets_of_three)

    counts
    |> Map.put(5, sets_of_five - recombinations)
    |> Map.put(3, sets_of_three - recombinations)
    |> Map.update(4, recombinations * 2, &(&1 + recombinations * 2))
  end

  defp recombine_sets_of_three_and_five(counts), do: counts

  defp add_group_price({size, count}, total) do
    total + count * Map.fetch!(@price_for_sets, size)
  end
end
