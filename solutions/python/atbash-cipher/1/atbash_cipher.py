"""Encode and decode text using the Atbash substitution cipher."""

from string import ascii_lowercase, digits


TRANSLATION = str.maketrans(ascii_lowercase, ascii_lowercase[::-1])
ALPHANUMERIC = frozenset(ascii_lowercase + digits)


def encode(plain_text: str) -> str:
    """Encode text in groups of five letters or digits."""
    cipher = decode(plain_text)
    return " ".join(cipher[index:index + 5] for index in range(0, len(cipher), 5))


def decode(ciphered_text: str) -> str:
    """Reverse the alphabet substitution and discard non-alphanumeric characters."""
    cleaned = "".join(char for char in ciphered_text.lower() if char in ALPHANUMERIC)
    return cleaned.translate(TRANSLATION)
