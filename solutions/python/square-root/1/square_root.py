"""Calculate integer square roots."""


def square_root(number):
    """Return the whole-number square root of number."""
    if number == 1:
        return 1

    low = 1
    high = number // 2

    while low <= high:
        candidate = (low + high) // 2
        square = candidate * candidate

        if square == number:
            return candidate

        if square < number:
            low = candidate + 1
        else:
            high = candidate - 1

    raise ValueError("Number does not have a whole-number square root.")
