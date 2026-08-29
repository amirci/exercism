"""Resistor color code helpers."""

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


def color_code(color):
    """Return the numeric code for a resistor color."""
    return COLORS.index(color)


def colors():
    """Return all resistor colors in code order."""
    return COLORS
