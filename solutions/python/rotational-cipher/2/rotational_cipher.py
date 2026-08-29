def rotate(text, key):
    return "".join(rotate_character(character, key) for character in text)


def rotate_character(character, key):
    if not character.isalpha():
        return character

    start = "a" if character.islower() else "A"
    return rotate_letter(character, key, start)


def rotate_letter(letter, key, start):
    return chr((ord(letter) - ord(start) + key) % 26 + ord(start))
