"""Measure an exact amount of liquid using two buckets."""

from collections import deque
from math import gcd


def _next_states(state, capacities, start_bucket):
    first, second = state
    capacity_one, capacity_two = capacities

    poured_to_two = min(first, capacity_two - second)
    poured_to_one = min(second, capacity_one - first)
    states = (
        (capacity_one, second),
        (first, capacity_two),
        (0, second),
        (first, 0),
        (first - poured_to_two, second + poured_to_two),
        (first + poured_to_one, second - poured_to_one),
    )

    forbidden = (
        (0, capacity_two) if start_bucket == "one"
        else (capacity_one, 0)
    )
    return [candidate for candidate in states if candidate not in (state, forbidden)]


def measure(bucket_one, bucket_two, goal, start_bucket):
    """Return the minimum actions, goal bucket, and remaining amount."""
    if bucket_one <= 0 or bucket_two <= 0 or goal <= 0:
        raise ValueError("Bucket capacities and goal must be positive.")
    if start_bucket not in {"one", "two"}:
        raise ValueError("start_bucket must be 'one' or 'two'.")
    if goal > max(bucket_one, bucket_two) or goal % gcd(bucket_one, bucket_two):
        raise ValueError("Goal cannot be reached.")

    start = (bucket_one, 0) if start_bucket == "one" else (0, bucket_two)
    queue = deque([(start, 1)])
    visited = {start}

    while queue:
        state, actions = queue.popleft()
        first, second = state
        if goal in state:
            if first == goal:
                return actions, "one", second
            return actions, "two", first

        for next_state in _next_states(state, (bucket_one, bucket_two), start_bucket):
            if next_state not in visited:
                visited.add(next_state)
                queue.append((next_state, actions + 1))

    raise ValueError("Goal cannot be reached.")
