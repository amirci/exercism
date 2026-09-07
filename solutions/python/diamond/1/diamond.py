"""Generate diamond rows for a given letter."""

START_LETTER = "A"


def rows(letter):
    """Return the diamond rows from A to letter and back."""
    letters = [chr(value) for value in range(ord(START_LETTER), ord(letter) + 1)]
    top = [_row_for(current, letter) for current in letters]

    return top + top[-2::-1]


def _row_for(current, widest):
    outer_spaces = " " * (ord(widest) - ord(current))

    if current == START_LETTER:
        return f"{outer_spaces}{current}{outer_spaces}"

    inner_spaces = " " * (2 * (ord(current) - ord(START_LETTER)) - 1)
    return f"{outer_spaces}{current}{inner_spaces}{current}{outer_spaces}"
