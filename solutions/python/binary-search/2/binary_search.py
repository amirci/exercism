"""Binary search implementation."""


def find(search_list, value):
    """Return the index of value in a sorted list."""
    left = 0
    right = len(search_list) - 1

    while left <= right:
        middle = (left + right) // 2
        middle_value = search_list[middle]

        if middle_value == value:
            return middle
        if value < middle_value:
            right = middle - 1
        else:
            left = middle + 1

    raise ValueError("value not in array")
