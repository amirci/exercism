"""Score bowling games using states that own their roll rules."""

from dataclasses import dataclass


def _validate_pin_range(pins):
    if not 0 <= pins <= 10:
        raise ValueError("pins must be between 0 and 10")


def _validate_standing_pins(first, pins):
    if first + pins > 10:
        raise ValueError("roll exceeds the number of standing pins")

def _is_strike(pins):
    return pins == 10

def _is_spare(prev, pins):
    return prev + pins == 10

def _is_last_frame(frame):
    return frame == 10

@dataclass(frozen=True)
class _FirstRoll:
    frame_number: int

    def next(self, pins, roll_index):
        """Start a frame, recording a strike immediately."""
        if _is_strike(pins):
            state = (
                _StrikeBonusFirst()
                if _is_last_frame(self.frame_number)
                else _FirstRoll(self.frame_number + 1)
            )
            return state, slice(roll_index, roll_index + 3)

        return _SecondRoll(self.frame_number, pins, roll_index), None


@dataclass(frozen=True)
class _SecondRoll:
    frame_number: int
    first: int
    frame_index: int

    def next(self, pins, unused_roll_index):
        """Finish a frame and determine whether a spare bonus is due."""
        _validate_standing_pins(self.first, pins)
        is_spare = _is_spare(self.first, pins)
        frame = slice(self.frame_index, self.frame_index + (3 if is_spare else 2))
        if _is_last_frame(self.frame_number):
            state = _SpareBonus() if is_spare else _Complete()
        else:
            state = _FirstRoll(self.frame_number + 1)

        return state, frame


@dataclass(frozen=True)
class _SpareBonus:
    @staticmethod
    def next(unused_pins, unused_roll_index):
        """Complete the game after the single spare fill ball."""
        return _Complete(), None


@dataclass(frozen=True)
class _StrikeBonusFirst:
    @staticmethod
    def next(pins, unused_roll_index):
        """Remember the first strike fill ball for pin validation."""
        return _StrikeBonusSecond(pins), None


@dataclass(frozen=True)
class _StrikeBonusSecond:
    first: int

    def next(self, pins, unused_roll_index):
        """Validate the remaining pins and complete the game."""
        if not _is_strike(self.first):
            _validate_standing_pins(self.first, pins)

        return _Complete(), None


@dataclass(frozen=True)
class _Complete:
    @staticmethod
    def next(pins, roll_index):
        """Reject every roll after completion."""
        raise ValueError("cannot roll after the game is complete")


class BowlingGame:
    """Track legal rolls and score a completed ten-frame game."""

    def __init__(self):
        self._rolls = []
        self._frames = []
        self._state = _FirstRoll(1)

    def roll(self, pins):
        """Record a roll, rejecting invalid pins or rolls after completion."""
        _validate_pin_range(pins)
        self._state, potential_frame = self._state.next(pins, len(self._rolls))
        self._rolls.append(pins)
        if potential_frame:
            self._frames.append(potential_frame)

    def score(self):
        """Return the final score once all frames and fill balls are complete."""
        if not isinstance(self._state, _Complete):
            raise TypeError("cannot score an incomplete game")
        return sum(self._frame_score(frame) for frame in self._frames)

    def _frame_score(self, frame):
        return sum(self._rolls[frame])
