"""Score bowling games with explicit roll states and frame scoring windows."""

from enum import Enum, auto


class _State(Enum):
    FIRST_ROLL = auto()
    SECOND_ROLL = auto()
    SPARE_BONUS = auto()
    STRIKE_BONUS_FIRST = auto()
    STRIKE_BONUS_SECOND = auto()
    COMPLETE = auto()


class BowlingGame:
    """Track legal rolls and score a completed ten-frame game."""

    def __init__(self):
        self._rolls = []
        self._frames = []
        self._state = _State.FIRST_ROLL
        self._first = 0

    def roll(self, pins):
        """Record a roll, rejecting invalid pins or rolls after completion."""
        self._validate_roll(pins)
        self._advance(pins)
        self._rolls.append(pins)

    def score(self):
        """Return the final score once all frames and fill balls are complete."""
        if self._state != _State.COMPLETE:
            raise ValueError("cannot score an incomplete game")
        return sum(sum(self._rolls[frame]) for frame in self._frames)

    def _validate_roll(self, pins):
        self._validate_pin_range(pins)
        self._validate_game_in_progress()
        self._validate_standing_pins(pins)

    @staticmethod
    def _validate_pin_range(pins):
        if not 0 <= pins <= 10:
            raise ValueError("pins must be between 0 and 10")

    def _validate_game_in_progress(self):
        if self._state == _State.COMPLETE:
            raise ValueError("cannot roll after the game is complete")

    def _validate_standing_pins(self, pins):
        if (
            self._state == _State.SECOND_ROLL
            or (self._state == _State.STRIKE_BONUS_SECOND and self._first != 10)
        ) and self._first + pins > 10:
            raise ValueError("roll exceeds the number of standing pins")

    def _advance(self, pins):
        match self._state:
            case _State.FIRST_ROLL:
                self._start_frame(pins)
            case _State.SECOND_ROLL:
                self._finish_frame(pins)
            case _State.STRIKE_BONUS_FIRST:
                self._first = pins
                self._state = _State.STRIKE_BONUS_SECOND
            case _State.SPARE_BONUS | _State.STRIKE_BONUS_SECOND:
                self._state = _State.COMPLETE

    def _start_frame(self, pins):
        if pins == 10:
            index = len(self._rolls)
            self._frames.append(slice(index, index + 3))
            if len(self._frames) == 10:
                self._state = _State.STRIKE_BONUS_FIRST
        else:
            self._first = pins
            self._state = _State.SECOND_ROLL

    def _finish_frame(self, pins):
        is_spare = self._first + pins == 10
        index = len(self._rolls) - 1
        self._frames.append(slice(index, index + (3 if is_spare else 2)))
        if len(self._frames) == 10:
            self._state = _State.SPARE_BONUS if is_spare else _State.COMPLETE
        else:
            self._state = _State.FIRST_ROLL
