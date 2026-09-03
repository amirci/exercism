"""Calculate sums of unique multiples below a limit."""


def sum_of_multiples(limit, multiples):
    """Return the sum of unique multiples of the given factors below limit."""
    return sum(
        {
            multiple
            for factor in multiples
            if factor != 0
            for multiple in range(factor, limit, factor)
        }
    )
