"""Compute the prime factors of natural numbers."""


def factors(value):
    """Return prime factors in ascending order, including repetitions."""
    prime_factors = []
    divisor = 2

    while divisor * divisor <= value:
        while value % divisor == 0:
            prime_factors.append(divisor)
            value //= divisor
        divisor += 1

    if value > 1:
        prime_factors.append(value)

    return prime_factors
