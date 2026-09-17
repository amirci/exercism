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
    match colors:
        case ["black"]:
            return "0 ohms"
        case [first, second, multiplier, tolerance]:
            value_bands = [first, second]
        case [first, second, third, multiplier, tolerance]:
            value_bands = [first, second, third]
        case _:
            raise ValueError("Unsupported resistor format")

    ohms = _resistance_value(value_bands) * 10 ** COLOR_CODES[multiplier]
    value, unit = _scaled_value(ohms)

    return f"{value:g} {unit} ±{TOLERANCES[tolerance]}%"


def _resistance_value(colors):
    """Return the numeric value represented by the significant color bands."""
    return sum(COLOR_CODES[color] * 10 ** index for index, color in enumerate(reversed(colors)))


def _scaled_value(ohms):
    """Return the resistance value scaled to the largest useful unit."""
    scale, unit = next((scale, unit) for scale, unit in SCALES if ohms >= scale)
    return ohms / scale, unit
