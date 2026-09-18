"""Encode and decode text with an affine cipher."""

from math import gcd


ALPHABET_SIZE = 26


def _validate_key(multiplier):
    if gcd(multiplier, ALPHABET_SIZE) != 1:
        raise ValueError("a and m must be coprime.")


def _inverse(multiplier):
    return next(
        candidate
        for candidate in range(ALPHABET_SIZE)
        if multiplier * candidate % ALPHABET_SIZE == 1
    )


def _transform(character, multiplier, shift):
    if character.isdigit():
        return character

    index = ord(character) - ord("a")
    encrypted_index = (multiplier * index + shift) % ALPHABET_SIZE
    return chr(ord("a") + encrypted_index)


def _letters_and_digits(text):
    return (character.lower() for character in text if character.isalnum())


def _group(text):
    return " ".join(text[index:index + 5] for index in range(0, len(text), 5))


def encode(plain_text, a, b):
    """Encode plain text using an affine cipher."""
    _validate_key(a)
    encoded = "".join(_transform(character, a, b) for character in _letters_and_digits(plain_text))
    return _group(encoded)


def decode(ciphered_text, a, b):
    """Decode ciphered text using an affine cipher."""
    _validate_key(a)
    inverse = _inverse(a)

    def decrypt(character):
        if character.isdigit():
            return character

        index = ord(character) - ord("a")
        decrypted_index = inverse * (index - b) % ALPHABET_SIZE
        return chr(ord("a") + decrypted_index)

    return "".join(decrypt(character) for character in _letters_and_digits(ciphered_text))
