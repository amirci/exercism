"""Calculate differences between sums and squares."""


def square_of_sum(number):
    """Return the square of the sum of the first natural numbers."""
    total = number * (number + 1) // 2
    return total * total


def sum_of_squares(number):
    """Return the sum of the squares of the first natural numbers."""
    return number * (number + 1) * (2 * number + 1) // 6


def difference_of_squares(number):
    """Return the difference between square of sum and sum of squares."""
    return square_of_sum(number) - sum_of_squares(number)
