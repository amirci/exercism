"""Spiral matrix generator."""


def spiral_matrix(size):
    """Return a square matrix filled in clockwise spiral order."""
    matrix = [[0] * size for _ in range(size)]
    top = 0
    bottom = size - 1
    left = 0
    right = size - 1
    value = 1

    while left <= right and top <= bottom:
        for column in range(left, right + 1):
            matrix[top][column] = value
            value += 1
        top += 1

        for row in range(top, bottom + 1):
            matrix[row][right] = value
            value += 1
        right -= 1

        for column in range(right, left - 1, -1):
            matrix[bottom][column] = value
            value += 1
        bottom -= 1

        for row in range(bottom, top - 1, -1):
            matrix[row][left] = value
            value += 1
        left += 1

    return matrix
