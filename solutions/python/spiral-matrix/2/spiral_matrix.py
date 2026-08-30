"""Spiral matrix generator."""

from itertools import cycle

DIRECTIONS = ((0, 1), (1, 0), (0, -1), (-1, 0))


def spiral_matrix(size):
    """Return a square matrix filled in clockwise spiral order."""
    matrix = [[None] * size for _ in range(size)]
    directions = cycle(DIRECTIONS)
    row = 0
    column = 0
    row_delta, column_delta = next(directions)

    for value in range(1, size * size + 1):
        matrix[row][column] = value

        if should_turn(matrix, size, (row, column), (row_delta, column_delta)):
            row_delta, column_delta = next(directions)

        row += row_delta
        column += column_delta

    return matrix


def should_turn(matrix, size, position, direction):
    """Return whether the next cursor movement needs to turn."""
    row, column = position
    row_delta, column_delta = direction
    next_row = row + row_delta
    next_column = column + column_delta

    return (
        not 0 <= next_row < size
        or not 0 <= next_column < size
        or matrix[next_row][next_column] is not None
    )
