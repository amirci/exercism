"""Find shortest paths through parent, child, and sibling relationships."""

from collections import defaultdict, deque
from itertools import combinations


class RelativeDistance:  # pylint: disable=too-few-public-methods
    """Store family connections for repeated distance queries."""

    def __init__(self, family_tree: dict[str, list[str]]) -> None:
        self._neighbors: dict[str, set[str]] = defaultdict(set, {
            parent: set() for parent in family_tree
        })

        for parent, children in family_tree.items():
            for child in children:
                self._connect(parent, child)
            for sibling_a, sibling_b in combinations(children, 2):
                self._connect(sibling_a, sibling_b)

    def _connect(self, person_a: str, person_b: str) -> None:
        self._neighbors[person_a].add(person_b)
        self._neighbors[person_b].add(person_a)

    def _validate_people(self, person_a: str, person_b: str) -> None:
        if person_a not in self._neighbors:
            raise ValueError("Person A not in family tree.")
        if person_b not in self._neighbors:
            raise ValueError("Person B not in family tree.")

    def degree_of_separation(self, person_a: str, person_b: str) -> int:
        """Return the shortest distance, raising if either person is unreachable."""
        self._validate_people(person_a, person_b)

        queue = deque([(person_a, 0)])

        visited = {person_a}

        while queue:
            person, distance = queue.popleft()

            if person == person_b:
                return distance

            for neighbor in self._neighbors[person]:
                if neighbor not in visited:
                    visited.add(neighbor)
                    queue.append((neighbor, distance + 1))

        raise ValueError("No connection between person A and person B.")
