"""Resistor color trio helpers."""

COLOR_CODES = {
    "black": 0,
    "brown": 1,
    "red": 2,
    "orange": 3,
    "yellow": 4,
    "green": 5,
    "blue": 6,
    "violet": 7,
    "grey": 8,
    "white": 9,
}

UNITS = ["ohms", "kiloohms", "megaohms", "gigaohms"]


def label(colors):
    """Return the resistance label for the first three colors."""
    first, second, multiplier = colors[:3]
    ohms = (COLOR_CODES[first] * 10 + COLOR_CODES[second]) * 10 ** COLOR_CODES[multiplier]
    value, unit = scaled_value(ohms)
    return f"{value} {unit}"


def scaled_value(ohms):
    """Return the resistance value scaled to the largest exact unit."""
    unit_index = 0
    while ohms >= 1000 and ohms % 1000 == 0:
        ohms //= 1000
        unit_index += 1
    return ohms, UNITS[unit_index]
