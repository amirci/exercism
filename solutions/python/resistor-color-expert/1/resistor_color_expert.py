"""Resistor color expert helpers."""

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

TOLERANCES = {
    "grey": "0.05",
    "violet": "0.1",
    "blue": "0.25",
    "green": "0.5",
    "brown": "1",
    "red": "2",
    "gold": "5",
    "silver": "10",
}

SCALES = [
    (1_000_000_000, "gigaohms"),
    (1_000_000, "megaohms"),
    (1_000, "kiloohms"),
    (1, "ohms"),
]


def resistor_label(colors):
    """Return a human-readable label for a resistor color band list."""
    if colors == ["black"]:
        return "0 ohms"

    *value_bands, multiplier, tolerance = colors
    ohms = resistance_value(value_bands) * 10 ** COLOR_CODES[multiplier]
    value, unit = scaled_value(ohms)

    return f"{format_number(value)} {unit} ±{TOLERANCES[tolerance]}%"


def resistance_value(colors):
    """Return the numeric value represented by the significant color bands."""
    return sum(COLOR_CODES[color] * 10 ** index for index, color in enumerate(reversed(colors)))


def scaled_value(ohms):
    """Return the resistance value scaled to the largest useful unit."""
    scale, unit = next((scale, unit) for scale, unit in SCALES if ohms >= scale)
    return ohms / scale, unit


def format_number(number):
    """Format a number without unnecessary trailing zeroes."""
    return f"{number:g}"
