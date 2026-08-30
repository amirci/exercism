"""Secret handshake command decoder."""

ACTIONS = [
    ("00001", "wink"),
    ("00010", "double blink"),
    ("00100", "close your eyes"),
    ("01000", "jump"),
]

REVERSE = "10000"


def commands(binary_str):
    """Return the handshake commands encoded by a binary string."""
    value = int(binary_str, 2)
    result = [action for bit, action in ACTIONS if has_bit(value, bit)]

    return result[::-1] if has_bit(value, REVERSE) else result


def has_bit(value, bit):
    """Return whether the bit mask is set in value."""
    return value & int(bit, 2)
