defmodule Poker do
  @rank_values %{
    "2" => 2,
    "3" => 3,
    "4" => 4,
    "5" => 5,
    "6" => 6,
    "7" => 7,
    "8" => 8,
    "9" => 9,
    "10" => 10,
    "J" => 11,
    "Q" => 12,
    "K" => 13,
    "A" => 14
  }

  @category_values %{
    high_card: 0,
    one_pair: 1,
    two_pair: 2,
    three_of_a_kind: 3,
    straight: 4,
    flush: 5,
    full_house: 6,
    four_of_a_kind: 7,
    straight_flush: 8
  }

  @doc """
  Given a list of poker hands, return the highest scoring hand or hands.
  """
  @spec best_hand(list(list(String.t()))) :: list(list(String.t()))
  def best_hand(hands) do
    hands
    |> Enum.map(&{&1, score(&1)})
    |> Enum.sort_by(&elem(&1, 1), :desc)
    |> best_scored_hands()
  end

  defp best_scored_hands([]), do: []

  defp best_scored_hands([{_hand, best_score} | _] = scored_hands) do
    scored_hands
    |> Enum.take_while(fn {_hand, score} -> score == best_score end)
    |> Enum.map(&elem(&1, 0))
  end

  defp score(hand) do
    cards = Enum.map(hand, &parse_card/1)
    ranks = Enum.map(cards, &elem(&1, 0))
    frequencies = Enum.frequencies(ranks)
    flush? = Enum.uniq(Enum.map(cards, &elem(&1, 1))) |> length() == 1
    straight_high = straight_high(ranks)

    category = hand_category(frequencies, flush?, straight_high)

    {
      Map.fetch!(@category_values, category),
      tie_breakers(category, frequencies, ranks, straight_high)
    }
  end

  defp parse_card(card) do
    {rank, suit} = String.split_at(card, -1)
    {Map.fetch!(@rank_values, rank), suit}
  end

  defp hand_category(_frequencies, true, high) when not is_nil(high), do: :straight_flush
  defp hand_category(_frequencies, _flush?, high) when not is_nil(high), do: :straight
  defp hand_category(_frequencies, true, _straight_high), do: :flush

  defp hand_category(frequencies, _flush?, _straight_high) do
    case frequencies |> Map.values() |> Enum.sort() do
      [1, 4] -> :four_of_a_kind
      [2, 3] -> :full_house
      [1, 1, 3] -> :three_of_a_kind
      [1, 2, 2] -> :two_pair
      [1, 1, 1, 2] -> :one_pair
      _ -> :high_card
    end
  end

  defp tie_breakers(:straight, _frequencies, _ranks, high), do: [high]
  defp tie_breakers(:straight_flush, _frequencies, _ranks, high), do: [high]

  defp tie_breakers(:four_of_a_kind, frequencies, _ranks, _straight_high) do
    [rank_with_count(frequencies, 4), rank_with_count(frequencies, 1)]
  end

  defp tie_breakers(:full_house, frequencies, _ranks, _straight_high) do
    [rank_with_count(frequencies, 3), rank_with_count(frequencies, 2)]
  end

  defp tie_breakers(:three_of_a_kind, frequencies, _ranks, _straight_high) do
    [rank_with_count(frequencies, 3) | ranks_with_count(frequencies, 1)]
  end

  defp tie_breakers(:two_pair, frequencies, _ranks, _straight_high) do
    pairs = ranks_with_count(frequencies, 2)
    [Enum.max(pairs), Enum.min(pairs), rank_with_count(frequencies, 1)]
  end

  defp tie_breakers(:one_pair, frequencies, _ranks, _straight_high) do
    [rank_with_count(frequencies, 2) | ranks_with_count(frequencies, 1)]
  end

  defp tie_breakers(:flush, _frequencies, ranks, _straight_high), do: Enum.sort(ranks, :desc)
  defp tie_breakers(:high_card, _frequencies, ranks, _straight_high), do: Enum.sort(ranks, :desc)

  defp rank_with_count(frequencies, count) do
    frequencies
    |> Enum.find(fn {_rank, frequency} -> frequency == count end)
    |> elem(0)
  end

  defp ranks_with_count(frequencies, count) do
    frequencies
    |> Enum.filter(fn {_rank, frequency} -> frequency == count end)
    |> Enum.map(&elem(&1, 0))
    |> Enum.sort(:desc)
  end

  defp straight_high(ranks) do
    sorted = Enum.uniq(ranks) |> Enum.sort()

    cond do
      sorted == [2, 3, 4, 5, 14] ->
        5

      length(sorted) == 5 and
          Enum.chunk_every(sorted, 2, 1, :discard)
          |> Enum.all?(fn [first, second] -> second == first + 1 end) ->
        List.last(sorted)

      true ->
        nil
    end
  end
end
