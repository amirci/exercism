"""Secret handshake command decoder."""

ACTIONS = {
    1: "wink",
    2: "double blink",
    4: "close your eyes",
    8: "jump",
}

REVERSE = 16


def commands(binary_str):
    """Return the handshake commands encoded by a binary string."""
    value = int(binary_str, 2)
    result = [action for bit, action in ACTIONS.items() if has_bit(value, bit)]

    return result[::-1] if has_bit(value, REVERSE) else result


def has_bit(value, bit):
    """Return whether the bit mask is set in value."""
    return value & bit
