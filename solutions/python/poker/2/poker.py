"""Choose the best poker hands from a collection of five-card hands."""

from collections import Counter
from typing import NamedTuple


VALUES = {str(value): value for value in range(2, 11)} | {"J": 11, "Q": 12, "K": 13, "A": 14}


class RankGroup(NamedTuple):
    """A rank and the number of times it occurs in a hand."""

    count: int
    rank: int


def _card_values(hand: str) -> tuple[list[int], list[str]]:
    """Return the ranks and suits in a hand."""
    cards = [(VALUES[card[:-1]], card[-1]) for card in hand.split()]
    return [value for value, _ in cards], [suit for _, suit in cards]


def _straight_high(values: list[int]) -> int:
    """Return the high card of a straight, or zero when there is none."""
    unique = set(values)
    if unique == {14, 2, 3, 4, 5}:
        return 5
    if len(unique) == 5 and max(unique) - min(unique) == 4:
        return max(unique)
    return 0


def _rank_groups(values: list[int]) -> list[RankGroup]:
    """Return rank groups ordered by count and rank."""
    counts = Counter(values)
    return sorted(
        (RankGroup(count, value) for value, count in counts.items()),
        reverse=True,
    )


def _hand_key(hand: str) -> tuple:
    """Return a comparable category and tie-break key for a hand."""
    values, suits = _card_values(hand)
    values.sort(reverse=True)
    groups = _rank_groups(values)
    first, second = groups[:2]
    flush = len(set(suits)) == 1
    straight_high = _straight_high(values)

    if flush and straight_high:
        category, tiebreak = 8, (straight_high,)
    elif first.count == 4:
        category, tiebreak = 7, (first.rank, second.rank)
    elif first.count == 3 and second.count == 2:
        category, tiebreak = 6, (first.rank, second.rank)
    elif flush:
        category, tiebreak = 5, tuple(values)
    elif straight_high:
        category, tiebreak = 4, (straight_high,)
    elif first.count == 3:
        kickers = sorted((value for value in values if value != first.rank), reverse=True)
        category, tiebreak = 3, (first.rank, *kickers)
    elif first.count == 2 and second.count == 2:
        pairs = sorted((first.rank, second.rank), reverse=True)
        kicker = groups[2].rank
        category, tiebreak = 2, (*pairs, kicker)
    elif first.count == 2:
        pair = first.rank
        kickers = sorted((value for value in values if value != pair), reverse=True)
        category, tiebreak = 1, (pair, *kickers)
    else:
        category, tiebreak = 0, tuple(values)

    return (category, *tiebreak)


def best_hands(hands):
    """Return all hands tied for the highest poker ranking."""
    scored_hands = [(hand, _hand_key(hand)) for hand in hands]
    best_score = max(score for _, score in scored_hands)
    return [hand for hand, score in scored_hands if score == best_score]
