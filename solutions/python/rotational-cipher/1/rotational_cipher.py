def rotate(text, key):
    return "".join(rotate_character(character, key) for character in text)


def rotate_character(character, key):
    if character.islower():
        return rotate_letter(character, key, "a")
    if character.isupper():
        return rotate_letter(character, key, "A")
    return character


def rotate_letter(letter, key, start):
    return chr((ord(letter) - ord(start) + key) % 26 + ord(start))
