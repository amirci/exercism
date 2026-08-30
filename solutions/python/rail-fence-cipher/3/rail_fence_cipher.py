"""Rail fence cipher encoder and decoder."""


def encode(message, rails):
    """Encode a message using the rail fence cipher."""
    return "".join(
        message[position[1]] for position in fence_pattern(rails, len(message))
    )


def decode(encoded_message, rails):
    """Decode a message encoded with the rail fence cipher."""
    encoded_positions = zip(fence_pattern(rails, len(encoded_message)), encoded_message)
    return "".join(
        character
        for position, character in sorted(encoded_positions, key=original_index)
    )


def fence_pattern(rails, message_size):
    """Return message positions sorted by rail order."""
    cycle_length = 2 * (rails - 1)

    return sorted(
        (rail_for(index, cycle_length), index) for index in range(message_size)
    )


def rail_for(index, cycle_length):
    """Return the rail number for a message index."""
    cycle_index = index % cycle_length
    return min(cycle_index, cycle_length - cycle_index)


def original_index(item):
    """Return the original message index for an encoded position."""
    return item[0][1]
