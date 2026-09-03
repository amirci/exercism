"""Calculate the next generation in Conway's Game of Life."""

from collections import Counter

ALIVE = 1
NEIGHBOR_DELTAS = (
    (-1, -1), (-1, 0), (-1, 1),
    (0, -1), (0, 1),
    (1, -1), (1, 0), (1, 1),
)


def tick(matrix):
    """Return the next generation of the matrix."""
    alive_cells = _alive_cells(matrix)
    neighbor_counts = _neighbor_counts(alive_cells, matrix)

    return [
        [
            int(_alive_next_generation((row, column), alive_cells, neighbor_counts))
            for column in range(_width(matrix))
        ]
        for row in range(_height(matrix))
    ]


def _alive_cells(matrix):
    return {
        (row, column)
        for row, cells in enumerate(matrix)
        for column, value in enumerate(cells)
        if value == ALIVE
    }


def _neighbor_counts(alive_cells, matrix):
    return Counter(
        neighbor
        for cell in alive_cells
        for neighbor in _neighbors(cell, matrix)
    )


def _alive_next_generation(cell, alive_cells, neighbor_counts):
    live_neighbors = neighbor_counts[cell]

    return live_neighbors == 3 or (cell in alive_cells and live_neighbors == 2)


def _neighbors(cell, matrix):
    row, column = cell

    return (
        (row + row_delta, column + column_delta)
        for row_delta, column_delta in NEIGHBOR_DELTAS
        if _in_bounds(matrix, row + row_delta, column + column_delta)
    )


def _in_bounds(matrix, row, column):
    return 0 <= row < _height(matrix) and 0 <= column < _width(matrix)


def _height(matrix):
    return len(matrix)


def _width(matrix):
    return len(matrix[0]) if matrix else 0
