"""Implement basic list operations."""

# pylint: disable=redefined-builtin


def append(list1, list2):
    """Return list1 followed by list2."""
    return foldl(_append_item, list2, list(list1))


def concat(lists):
    """Flatten one level of nested lists."""
    return foldl(append, lists, [])


def filter(function, items):
    """Return items for which function returns true."""
    def keep_if(result, item):
        return _append_item(result, item) if function(item) else result

    return foldl(keep_if, items, [])


def length(items):
    """Return the number of items."""
    count = 0

    for _ in items:
        count += 1

    return count


def map(function, items):
    """Return the result of applying function to each item."""
    def append_mapped(result, item):
        result.append(function(item))
        return result

    return foldl(append_mapped, items, [])


def foldl(function, items, initial):
    """Fold items into the accumulator from the left."""
    result = initial

    for item in items:
        result = function(result, item)

    return result


def foldr(function, items, initial):
    """Fold items into the accumulator from the right."""
    return foldl(function, reverse(items), initial)


def reverse(items):
    """Return items in reverse order."""
    def prepend(result, item):
        result.insert(0, item)
        return result

    return foldl(prepend, items, [])


def _append_item(result, item):
    result.append(item)
    return result
