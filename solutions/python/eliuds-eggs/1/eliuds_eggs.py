"""Count eggs from a binary display value."""


def egg_count(display_value):
    """Return the number of 1 bits in display_value."""
    eggs = 0

    while display_value > 0:
        eggs += display_value & 1
        display_value >>= 1

    return eggs
