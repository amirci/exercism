"""Search files for lines matching a given pattern and set of flags."""

from dataclasses import dataclass
from typing import Callable


@dataclass(frozen=True)
class GrepOptions:
    """Store options that control how grep processes lines."""

    line_matches: Callable[[str, str], bool]
    format_line: Callable[[str, int, str], str]
    only_filenames: bool


def grep(pattern, flags, files):
    """Search files for lines matching a pattern and flags."""
    options = _parse_options(flags, files)

    return "".join(_matching_output(filename, pattern, options) for filename in files)


def _parse_options(flags, files):
    values = flags.split()
    return GrepOptions(
        line_matches=_line_matcher(values),
        format_line=_line_formatter(values, files),
        only_filenames="-l" in values,
    )


def _matching_output(filename, pattern, options):
    matches = list(_matching_lines(filename, pattern, options))

    if options.only_filenames:
        return f"{filename}\n" if matches else ""

    return "".join(matches)


def _matching_lines(filename, pattern, options):
    with open(filename, encoding="utf-8") as file:
        for line_number, line in enumerate(file, start=1):
            if options.line_matches(line, pattern):
                yield options.format_line(filename, line_number, line)


def _line_matcher(flags):
    def contains_pattern(line, pattern):
        return pattern in line

    def equals_pattern(line, pattern):
        return line.rstrip("\n") == pattern

    matcher = equals_pattern if "-x" in flags else contains_pattern

    if "-i" in flags:
        matcher = _case_insensitive(matcher)

    if "-v" in flags:
        matcher = _inverted(matcher)

    return matcher


def _case_insensitive(matcher):
    def wrapped(line, pattern):
        return matcher(line.lower(), pattern.lower())

    return wrapped


def _inverted(matcher):
    def wrapped(line, pattern):
        return not matcher(line, pattern)

    return wrapped


def _line_formatter(flags, files):
    def filename_prefix(filename, _line_number):
        return f"{filename}:" if len(files) > 1 else ""

    def line_number_prefix(_filename, line_number):
        return f"{line_number}:" if "-n" in flags else ""

    def format_line(filename, line_number, line):
        return (
            filename_prefix(filename, line_number)
            + line_number_prefix(filename, line_number)
            + line
        )

    return format_line
