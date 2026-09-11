"""Track guesses and outcomes for a Hangman game."""

STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    """Maintain a word, guessed letters, and remaining failures."""

    def __init__(self, word: str) -> None:
        self.word = word
        self.guessed_chars: set[str] = set()
        self.remaining_guesses = 9
        self.status = STATUS_ONGOING

    def guess(self, char: str) -> None:
        """Record a guess, penalizing incorrect or repeated letters."""
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")

        if self._is_failed_guess(char):
            self.remaining_guesses -= 1

        self.guessed_chars.add(char)

        self.status = self._next_status()

    def _next_status(self) -> str:
        if self._is_word_guessed():
            status = STATUS_WIN
        elif self.remaining_guesses < 0:
            status = STATUS_LOSE
        else:
            status = STATUS_ONGOING
        return status

    def _is_failed_guess(self, char: str) -> bool:
        return char in self.guessed_chars or char not in self.word

    def _is_word_guessed(self) -> bool:
        return set(self.word).issubset(self.guessed_chars)

    def get_masked_word(self) -> str:
        """Reveal guessed letters and mask the rest with underscores."""
        return "".join(
            letter if letter in self.guessed_chars else "_"
            for letter in self.word
        )

    def get_status(self) -> str:
        """Return whether the game is ongoing, won, or lost."""
        return self.status
