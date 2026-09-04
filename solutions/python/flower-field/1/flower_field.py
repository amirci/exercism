"""Annotate a flower field with adjacent flower counts."""

FLOWER = "*"
EMPTY = " "
INVALID_BOARD = "The board is invalid with current input."
NEIGHBOR_DELTAS = (
    (-1, -1), (-1, 0), (-1, 1),
    (0, -1), (0, 1),
    (1, -1), (1, 0), (1, 1),
)


def annotate(garden):
    """Return the garden annotated with adjacent flower counts."""
    _validate(garden)

    return [
        "".join(_annotate_cell(garden, row, column) for column in range(_width(garden)))
        for row in range(_height(garden))
    ]


def _annotate_cell(garden, row, column):
    if garden[row][column] == FLOWER:
        return FLOWER

    adjacent_flowers = _adjacent_flower_count(garden, row, column)
    return str(adjacent_flowers) if adjacent_flowers else EMPTY


def _adjacent_flower_count(garden, row, column):
    return sum(
        garden[neighbor_row][neighbor_column] == FLOWER
        for neighbor_row, neighbor_column in _neighbors(garden, row, column)
    )


def _neighbors(garden, row, column):
    return (
        (row + row_delta, column + column_delta)
        for row_delta, column_delta in NEIGHBOR_DELTAS
        if _in_bounds(garden, row + row_delta, column + column_delta)
    )


def _in_bounds(garden, row, column):
    return 0 <= row < _height(garden) and 0 <= column < _width(garden)


def _validate(garden):
    if any(len(row) != _width(garden) for row in garden):
        raise ValueError(INVALID_BOARD)

    if any(cell not in (FLOWER, EMPTY) for row in garden for cell in row):
        raise ValueError(INVALID_BOARD)


def _height(garden):
    return len(garden)


def _width(garden):
    return len(garden[0]) if garden else 0
