ALPHABET = "abcdefghijklmnopqrstuvwxyz"


def rotate(text, key):
    rotated = ALPHABET[key:] + ALPHABET[:key]
    translation = str.maketrans(
        ALPHABET + ALPHABET.upper(),
        rotated + rotated.upper()
    )
    return text.translate(translation)
