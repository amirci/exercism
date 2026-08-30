"""Hamming distance calculation."""


def distance(strand_a, strand_b):
    """Return the Hamming distance between equal-length strands."""
    if len(strand_a) != len(strand_b):
        raise ValueError("Strands must be of equal length.")

    return sum(left != right for left, right in zip(strand_a, strand_b))
