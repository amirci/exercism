defmodule BookStore do
  @typedoc "A book is represented by its number in the 5-book series"
  @type book :: 1 | 2 | 3 | 4 | 5
  @type basket :: [book()]

  @doc """
  Calculate lowest price (in cents) for a shopping basket containing books.
  """
  @spec total(basket :: basket()) :: integer()
  def total(basket) do
    basket
    |> counts()
    |> minimum_price(%{})
    |> elem(0)
  end

  defp counts(basket) do
    frequencies = Enum.frequencies(basket)

    for book <- 1..5 do
      Map.get(frequencies, book, 0)
    end
  end

  defp minimum_price([0, 0, 0, 0, 0], memo), do: {0, memo}

  defp minimum_price(counts, memo) do
    case Map.fetch(memo, counts) do
      {:ok, price} ->
        {price, memo}

      :error ->
        {price, memo} =
          counts
          |> available_books()
          |> subsets()
          |> Enum.reject(&(&1 == []))
          |> Enum.reduce({nil, memo}, fn group, {best, memo} ->
            next_counts = remove_group(counts, group)
            {remaining_price, memo} = minimum_price(next_counts, memo)
            candidate = group_price(group) + remaining_price

            {minimum(best, candidate), memo}
          end)

        {price, Map.put(memo, counts, price)}
    end
  end

  defp available_books(counts) do
    counts
    |> Enum.with_index()
    |> Enum.filter(fn {count, _index} -> count > 0 end)
    |> Enum.map(fn {_count, index} -> index end)
  end

  defp subsets([]), do: [[]]

  defp subsets([head | tail]) do
    without_head = subsets(tail)
    without_head ++ Enum.map(without_head, &[head | &1])
  end

  defp remove_group(counts, group) do
    Enum.with_index(counts)
    |> Enum.map(fn {count, index} ->
      if index in group, do: count - 1, else: count
    end)
  end

  defp group_price(group) do
    size = length(group)
    discount = %{1 => 0, 2 => 5, 3 => 10, 4 => 20, 5 => 25}[size]
    div(size * 800 * (100 - discount), 100)
  end

  defp minimum(nil, candidate), do: candidate
  defp minimum(best, candidate), do: min(best, candidate)
end
