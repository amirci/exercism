"""Convert integers into their English names."""

from collections.abc import Iterator

ONES = (
    "zero", "one", "two", "three", "four", "five", "six", "seven",
    "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
)
TENS = ("", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")
SCALES = ("", "thousand", "million", "billion")
MAX_NUMBER = 999_999_999_999


def _say_under_thousand(number: int) -> str:
    if number < 20:
        return ONES[number]

    hundreds, remainder = divmod(number, 100)
    words = []
    if hundreds:
        words.extend((ONES[hundreds], "hundred"))
    if remainder:
        if remainder < 20:
            words.append(ONES[remainder])
        else:
            tens, ones = divmod(remainder, 10)
            words.append(TENS[tens] + (f"-{ONES[ones]}" if ones else ""))
    return " ".join(words)


def _chunks(number: int) -> list[int]:
    chunks = []

    while number:
        number, chunk = divmod(number, 1000)
        chunks.append(chunk)

    return chunks


def _numbered_chunks(number: int) -> Iterator[tuple[int, int]]:
    return reversed(list(enumerate(_chunks(number))))


def _say_chunk(chunk: int, scale: str) -> str:
    words = [word for word in (_say_under_thousand(chunk), scale) if word]
    return " ".join(words)


def say(number: int) -> str:
    """Return the English form for a number in the supported range."""
    if not 0 <= number <= MAX_NUMBER:
        raise ValueError("input out of range")

    if number == 0:
        return ONES[0]

    words = []
    for index, chunk in _numbered_chunks(number):
        if chunk:
            words.append(_say_chunk(chunk, SCALES[index]))

    return " ".join(words)
