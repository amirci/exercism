"""Translate English text into Pig Latin."""

VOWELS = "aeiou"
VOWEL_SOUNDS = (*VOWELS, "xr", "yt")


def translate(text):
    """Return the Pig Latin translation of text."""
    return " ".join(_translate_word(word) for word in text.split())


def _translate_word(word):
    if word.startswith(VOWEL_SOUNDS):
        return f"{word}ay"

    prefix_length = _prefix_length(word)
    return f"{word[prefix_length:]}{word[:prefix_length]}ay"


def _prefix_length(word):
    for index, character in enumerate(word):
        if word[index:index + 2] == "qu":
            return index + 2

        if character in VOWELS or (character == "y" and index > 0):
            return index

    return len(word)
