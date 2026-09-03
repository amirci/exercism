"""Transform legacy letter score data into a direct lookup."""


def transform(legacy_data):
    """Return a mapping from lowercase letters to their scores."""
    return {
        letter.lower(): score
        for score, letters in legacy_data.items()
        for letter in letters
    }
