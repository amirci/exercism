"""Rail fence cipher encoder and decoder."""


def encode(message, rails):
    """Encode a message using the rail fence cipher."""
    encoded_rails = [""] * rails

    for character, rail in zip(message, rail_pattern(len(message), rails)):
        encoded_rails[rail] += character

    return "".join(encoded_rails)


def decode(encoded_message, rails):
    """Decode a message encoded with the rail fence cipher."""
    pattern = rail_pattern(len(encoded_message), rails)
    rail_lengths = [pattern.count(rail) for rail in range(rails)]
    decoded_rails = split_by_lengths(encoded_message, rail_lengths)

    return "".join(decoded_rails[rail].pop(0) for rail in pattern)


def rail_pattern(length, rails):
    """Return the rail index used for each message position."""
    cycle = list(range(rails)) + list(range(rails - 2, 0, -1))
    return [cycle[index % len(cycle)] for index in range(length)]


def split_by_lengths(text, lengths):
    """Split text into lists of characters with the given lengths."""
    groups = []
    start = 0

    for length in lengths:
        end = start + length
        groups.append(list(text[start:end]))
        start = end

    return groups
