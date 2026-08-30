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

SCALES = [
    (1_000_000_000, "gigaohms"),
    (1_000_000, "megaohms"),
    (1_000, "kiloohms"),
    (1, "ohms"),
]


def label(colors):
    """Return the resistance label for the first three colors."""
    first, second, multiplier = colors[:3]
    ohms = (COLOR_CODES[first] * 10 + COLOR_CODES[second]) * 10 ** COLOR_CODES[multiplier]
    value, unit = scaled_value(ohms)
    return f"{value} {unit}"


def scaled_value(ohms):
    """Return the resistance value scaled to the largest exact unit."""
    return next(
        (
            (ohms // scale, unit)
            for scale, unit in SCALES
            if can_scale_to(ohms, scale)
        ),
        (ohms, "ohms"),
    )


def can_scale_to(ohms, scale):
    """Return whether ohms can be represented exactly at this scale."""
    return ohms >= scale and ohms % scale == 0
