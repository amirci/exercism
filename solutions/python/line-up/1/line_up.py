"""Line up sentence formatting."""

SUFFIXES = {
    1: "st",
    2: "nd",
    3: "rd",
}


def line_up(name, number):
    """Return a line-up message with the number as an ordinal."""
    return (
        f"{name}, you are the {number}{suffix_for(number)} "
        "customer we serve today. Thank you!"
    )


def suffix_for(number):
    """Return the ordinal suffix for a number."""
    if 11 <= number % 100 <= 13:
        return "th"

    return SUFFIXES.get(number % 10, "th")
