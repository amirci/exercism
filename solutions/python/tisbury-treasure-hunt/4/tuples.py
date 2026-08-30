"""Functions to help Azara and Rui locate pirate treasure."""


def get_coordinate(record):
    """Return coordinate value from a tuple containing the treasure name, and treasure coordinate.

    :param record: tuple - with a (treasure, coordinate) pair.
    :return: str - the extracted map coordinate.
    """

    return record[1]


def convert_coordinate(coordinate):
    """Split the given coordinate into tuple containing its individual components.

    :param coordinate: str - a string map coordinate
    :return: tuple - the string coordinate split into its individual components.
    """
    return tuple(coordinate)


def compare_records(azara, rui):
    """Compare two record types and determine if their coordinates match.

    :param azara_record: tuple - a (treasure, coordinate) pair.
    :param rui_record: tuple - a (location, tuple(coordinate_1, coordinate_2), quadrant) trio.
    :return: bool - do the coordinates match?
    """
    _, str_coord = azara
    _, coordinate, _ = rui

    return convert_coordinate(str_coord) == coordinate


def create_record(azara, rui):
    """Combine the two record types (if possible) and create a combined record group.

    :param azara: tuple - a (treasure, coordinate) pair.
    :param rui: tuple - a (location, coordinate, quadrant) trio.
    :return: tuple or str - the combined record (if compatible), or
    the string "not a match" (if incompatible).
    """
    return azara + rui if compare_records(azara, rui) else "not a match"


def clean_up(combined):
    """Clean up a combined record group into a multi-line string of single records.

    :param combined: tuple - everything from both participants.
    :return: str - everything "cleaned", excess coordinates and information are removed.

    The return statement should be a multi-lined string with items separated by newlines.

    (see HINTS.md for an example).
    """
    cleaned = [
        str((treasure, location, coordinate, quadrant))
        for treasure, _, location, coordinate, quadrant in combined
    ]

    return "\n".join(cleaned) + "\n"
