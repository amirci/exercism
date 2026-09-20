"""Choose the best poker hands from a collection of five-card hands."""

from collections import Counter


VALUES = {str(value): value for value in range(2, 11)} | {"J": 11, "Q": 12, "K": 13, "A": 14}


def _hand_key(hand: str) -> tuple:
    """Return a comparable category and tie-break key for a hand."""
    cards = [(VALUES[card[:-1]], card[-1]) for card in hand.split()]
    values = sorted((value for value, _ in cards), reverse=True)
    unique = set(values)

    counts = Counter(values)
    groups = sorted(((count, value) for value, count in counts.items()), reverse=True)
    flush = len({suit for _, suit in cards}) == 1
    straight_high = (
        5
        if unique == {14, 2, 3, 4, 5}
        else max(unique)
        if len(unique) == 5 and max(unique) - min(unique) == 4
        else 0
    )

    if flush and straight_high:
        category, tiebreak = 8, (straight_high,)
    elif groups[0][0] == 4:
        category, tiebreak = 7, (groups[0][1], groups[1][1])
    elif groups[0][0] == 3 and groups[1][0] == 2:
        category, tiebreak = 6, (groups[0][1], groups[1][1])
    elif flush:
        category, tiebreak = 5, tuple(values)
    elif straight_high:
        category, tiebreak = 4, (straight_high,)
    elif groups[0][0] == 3:
        kickers = sorted((value for value in values if value != groups[0][1]), reverse=True)
        category, tiebreak = 3, (groups[0][1], *kickers)
    elif groups[0][0] == 2 and groups[1][0] == 2:
        pairs = sorted((groups[0][1], groups[1][1]), reverse=True)
        kicker = groups[2][1]
        category, tiebreak = 2, (*pairs, kicker)
    elif groups[0][0] == 2:
        pair = groups[0][1]
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
