"""Calculate the moment one gigasecond after a given time."""

from datetime import timedelta

GIGASECOND = 1_000_000_000


def add(moment):
    """Return the date and time one gigasecond after moment."""
    return moment + timedelta(seconds=GIGASECOND)
