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
    |> Enum.map(&parsed_hand/1)
    |> Enum.sort_by(&{&1.category, &1.cards}, :desc)
    |> best_hands()
  end

  defp best_hands([]), do: []

  defp best_hands([best | _] = hands) do
    hands
    |> Enum.take_while(&same_score?(&1, best))
    |> Enum.map(& &1.hand)
  end

  defp same_score?(left, right), do: left.category == right.category and left.cards == right.cards

  defp parsed_hand(hand) do
    cards = Enum.map(hand, &parse_card/1)
    ranks = Enum.map(cards, &elem(&1, 0))
    frequencies = Enum.frequencies(ranks)
    flush? = flush?(cards)
    straight_high = straight_high(ranks)

    category = hand_category(frequencies, flush?, straight_high)
    category_value = Map.fetch!(@category_values, category)
    tie_breakers = tie_breakers(category, cards, frequencies, straight_high)

    %{hand: hand, category: category_value, cards: tie_breakers}
  end

  defp parse_card(card) do
    {rank, suit} = String.split_at(card, -1)
    {Map.fetch!(@rank_values, rank), suit}
  end

  defp flush?(cards), do: cards |> Enum.map(&elem(&1, 1)) |> Enum.uniq() |> length() == 1

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

  defp tie_breakers(:straight, _cards, _frequencies, high), do: [high]
  defp tie_breakers(:straight_flush, _cards, _frequencies, high), do: [high]

  defp tie_breakers(_category, cards, frequencies, _straight_high) do
    cards
    |> Enum.sort_by(fn {rank, _suit} -> {Map.fetch!(frequencies, rank), rank} end, :desc)
    |> Enum.map(&elem(&1, 0))
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
