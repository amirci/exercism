"""Extract contiguous digit slices from a series."""


def slices(series, length):
    """Return every contiguous substring of the requested length."""
    if not series:
        raise ValueError("series cannot be empty")
    if length == 0:
        raise ValueError("slice length cannot be zero")
    if length < 0:
        raise ValueError("slice length cannot be negative")
    if length > len(series):
        raise ValueError("slice length cannot be greater than series length")

    return [
        series[index:index + length]
        for index in range(len(series) - length + 1)
    ]
