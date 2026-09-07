"""Calculate integer square roots."""


def square_root(number):
    """Return the whole-number square root of number."""
    guess = number

    while guess * guess > number:
        guess = (guess + number // guess) // 2

    return guess
