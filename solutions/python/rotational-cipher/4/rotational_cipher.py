"""Rotational cipher implementation."""

ALPHABET = "abcdefghijklmnopqrstuvwxyz"


def rotate(text, key):
    """Rotate letters in text by key positions."""
    rotated = ALPHABET[key:] + ALPHABET[:key]
    translation = str.maketrans(
        ALPHABET + ALPHABET.upper(),
        rotated + rotated.upper()
    )
    return text.translate(translation)
