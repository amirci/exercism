"""Annotate a flower field with adjacent flower counts."""

from dataclasses import dataclass

FLOWER = "*"
EMPTY = " "
INVALID_BOARD = "The board is invalid with current input."
NEIGHBOR_DELTAS = (
    (-1, -1), (-1, 0), (-1, 1),
    (0, -1), (0, 1),
    (1, -1), (1, 0), (1, 1),
)


@dataclass(frozen=True)
class Garden:
    """A rectangular flower field."""

    cells: list[str]

    @property
    def height(self):
        """Return the number of rows."""
        return len(self.cells)

    @property
    def width(self):
        """Return the number of columns."""
        return len(self.cells[0]) if self.cells else 0

    def flower_at(self, row, column):
        """Return whether the given coordinate contains a flower."""
        return self.cells[row][column] == FLOWER


def annotate(garden):
    """Return the garden annotated with adjacent flower counts."""
    flower_field = Garden(garden)
    _validate(flower_field)

    return [
        "".join(
            _cell_annotation(flower_field, row, column)
            for column in range(flower_field.width)
        )
        for row in range(flower_field.height)
    ]


def _cell_annotation(garden, row, column):
    return (
        FLOWER
        if garden.flower_at(row, column)
        else _flower_count_annotation(garden, row, column)
    )


def _flower_count_annotation(garden, row, column):
    adjacent_flowers = _adjacent_flower_count(garden, row, column)
    return str(adjacent_flowers) if adjacent_flowers else EMPTY


def _adjacent_flower_count(garden, row, column):
    return sum(
        garden.flower_at(row, col)
        for row, col in _neighbors(garden, row, column)
    )


def _neighbors(garden, row, column):
    return (
        (row + row_delta, column + column_delta)
        for row_delta, column_delta in NEIGHBOR_DELTAS
        if _in_bounds(garden, row + row_delta, column + column_delta)
    )


def _in_bounds(garden, row, column):
    return 0 <= row < garden.height and 0 <= column < garden.width


def _validate(garden):
    if any(len(row) != garden.width for row in garden.cells):
        raise ValueError(INVALID_BOARD)

    if any(cell not in (FLOWER, EMPTY) for row in garden.cells for cell in row):
        raise ValueError(INVALID_BOARD)
