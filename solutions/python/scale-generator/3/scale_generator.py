"""Generate chromatic and interval-based musical scales."""

from itertools import accumulate, cycle, islice

SHARP_SCALE = ("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
FLAT_SCALE = ("A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab")
FLAT_TONICS = {"F", "Bb", "Eb", "Ab", "Db", "Gb", "d", "g", "c", "f", "bb", "eb"}
STEPS = {"m": 1, "M": 2, "A": 3}


class Scale:
    """A musical scale starting from a tonic."""

    def __init__(self, tonic):
        self.tonic = tonic[0].upper() + tonic[1:]
        self.scale = FLAT_SCALE if tonic in FLAT_TONICS else SHARP_SCALE

    def chromatic(self):
        """Return the chromatic scale starting from the tonic."""
        tonic_index = self.scale.index(self.tonic)
        return list(islice(cycle(self.scale), tonic_index, tonic_index + len(self.scale)))

    def interval(self, intervals):
        """Return the scale produced by following the given interval pattern."""
        chromatic_scale = self.chromatic()
        positions = accumulate(STEPS[interval] for interval in intervals)

        return [
            chromatic_scale[position % len(chromatic_scale)]
            for position in (0, *positions)
        ]
