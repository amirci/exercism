"""Resistor color duo helpers."""

COLORS = [
    "black",
    "brown",
    "red",
    "orange",
    "yellow",
    "green",
    "blue",
    "violet",
    "grey",
    "white",
]


def value(colors):
    """Return the resistor value represented by the first two colors."""
    first, second = colors[:2]
    return COLORS.index(first) * 10 + COLORS.index(second)
