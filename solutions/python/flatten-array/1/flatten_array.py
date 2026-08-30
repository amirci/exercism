"""Flatten nested lists while omitting None values."""


def flatten(iterable):
    """Return a flat list with None values removed."""
    result = []

    for item in iterable:
        if item is None:
            continue
        if isinstance(item, list):
            result.extend(flatten(item))
        else:
            result.append(item)

    return result
