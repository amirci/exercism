"""Solve the Zebra Puzzle and answer its two questions."""

from functools import partial
from itertools import permutations


COLORS = ("red", "green", "ivory", "yellow", "blue")
NATIONALITIES = ("Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese")
PETS = ("dog", "snails", "fox", "horse", "zebra")
DRINKS = ("coffee", "tea", "milk", "orange juice", "water")
HOBBIES = ("dancing", "painting", "reading", "football", "chess")


def _next_to(first, second):
    return abs(first - second) == 1


def _green_follows_ivory(colors):
    return colors.index("green") == colors.index("ivory") + 1


def _valid_nationalities(colors, nationalities):
    return (
        nationalities[0] == "Norwegian"
        and nationalities.index("Englishman") == colors.index("red")
    )


def _valid_drinks(colors, nationalities, drinks):
    return (
        drinks[2] == "milk"
        and drinks[colors.index("green")] == "coffee"
        and drinks[nationalities.index("Ukrainian")] == "tea"
    )


def _valid_arrangements(values, condition):
    return (
        arrangement
        for arrangement in permutations(values)
        if condition(arrangement)
    )


def _valid_color_arrangements():
    return _valid_arrangements(COLORS, _green_follows_ivory)


def _valid_nationality_arrangements(colors):
    return _valid_arrangements(
        NATIONALITIES,
        partial(_valid_nationalities, colors),
    )


def _valid_drink_arrangements(colors, nationalities):
    return _valid_arrangements(
        DRINKS,
        partial(_valid_drinks, colors, nationalities),
    )


def _valid_pets(nationalities, pets):
    return pets[nationalities.index("Spaniard")] == "dog"


def _valid_pet_arrangements(nationalities):
    return _valid_arrangements(PETS, partial(_valid_pets, nationalities))


def _valid_hobbies(colors, nationalities, drinks, pets, hobbies):
    return all(
        (
            hobbies[pets.index("snails")] == "dancing",
            hobbies[colors.index("yellow")] == "painting",
            drinks[hobbies.index("football")] == "orange juice",
            hobbies[nationalities.index("Japanese")] == "chess",
            _next_to(hobbies.index("reading"), pets.index("fox")),
            _next_to(hobbies.index("painting"), pets.index("horse")),
            _next_to(nationalities.index("Norwegian"), colors.index("blue")),
        )
    )


def _valid_hobby_arrangements(colors, nationalities, drinks, pets):
    return _valid_arrangements(
        HOBBIES,
        partial(_valid_hobbies, colors, nationalities, drinks, pets),
    )


def _make_houses(colors, nationalities, pets, drinks, hobbies):
    return [
        {
            "color": colors[index],
            "nationality": nationalities[index],
            "pet": pets[index],
            "drink": drinks[index],
            "hobby": hobbies[index],
        }
        for index in range(5)
    ]


def _find_hobby_solution(colors, nationalities, drinks, pets):
    for hobbies in _valid_hobby_arrangements(
        colors, nationalities, drinks, pets
    ):
        return _make_houses(colors, nationalities, pets, drinks, hobbies)

    return None


def _find_solution():
    for colors in _valid_color_arrangements():
        for nationalities in _valid_nationality_arrangements(colors):
            for drinks in _valid_drink_arrangements(colors, nationalities):
                for pets in _valid_pet_arrangements(nationalities):
                    solution = _find_hobby_solution(
                        colors, nationalities, drinks, pets
                    )
                    if solution is not None:
                        return solution

    raise ValueError("No solution found")


def drinks_water():
    """Return the nationality of the resident who drinks water."""
    return next(house["nationality"] for house in _find_solution() if house["drink"] == "water")


def owns_zebra():
    """Return the nationality of the resident who owns the zebra."""
    return next(house["nationality"] for house in _find_solution() if house["pet"] == "zebra")
