"""Check whether brackets are correctly paired."""

CLOSING_TO_OPENING = {
    ")": "(",
    "]": "[",
    "}": "{",
}
OPENING_BRACKETS = set(CLOSING_TO_OPENING.values())


def is_paired(input_string):
    """Return whether all brackets in input_string are paired."""
    stack = []

    for character in input_string:
        if character in OPENING_BRACKETS:
            stack.append(character)
        elif character in CLOSING_TO_OPENING:
            if not stack or stack.pop() != CLOSING_TO_OPENING[character]:
                return False

    return not stack
