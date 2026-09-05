"""
This exercise stub and the test suite contain several enumerated constants.

Enumerated constants can be done with a NAME assigned to an arbitrary,
but unique value. An integer is traditionally used because it’s memory
efficient.
It is a common practice to export both constants and functions that work with
those constants (ex. the constants in the os, subprocess and re modules).

You can learn more here: https://en.wikipedia.org/wiki/Enumerated_type
"""

# Possible sublist categories.
# Change the values as you see fit.
SUBLIST = "sublist"
SUPERLIST = "superlist"
EQUAL = "equal"
UNEQUAL = "unequal"


def sublist(list_one, list_two):
    """Return the relationship between two lists."""
    if list_one == list_two:
        return EQUAL
    if _contains(list_two, list_one):
        return SUBLIST
    if _contains(list_one, list_two):
        return SUPERLIST

    return UNEQUAL


def _contains(items, candidate):
    return not candidate or any(
        items[index:index + len(candidate)] == candidate
        for index in range(len(items) - len(candidate) + 1)
    )
