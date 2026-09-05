"""Search files for lines matching a given pattern and set of flags."""

from dataclasses import dataclass


@dataclass(frozen=True)
class GrepOptions:
    """Store options that control how grep processes lines."""

    case_insensitive: bool = False
    whole_line: bool = False
    invert: bool = False
    include_line_number: bool = False
    include_filename: bool = False
    only_filenames: bool = False


def grep(pattern, flags, files):
    """Search files for lines matching a pattern and flags."""
    options = GrepOptions(
        case_insensitive="-i" in flags,
        whole_line="-x" in flags,
        invert="-v" in flags,
        include_line_number="-n" in flags,
        include_filename=len(files) > 1,
        only_filenames="-l" in flags,
    )

    return "".join(_matching_output(filename, pattern, options) for filename in files)


def _matching_output(filename, pattern, options):
    matches = list(_matching_lines(filename, pattern, options))

    if options.only_filenames:
        return f"{filename}\n" if matches else ""

    return "".join(matches)


def _matching_lines(filename, pattern, options):
    with open(filename, encoding="utf-8") as file:
        for line_number, line in enumerate(file, start=1):
            if _line_matches(line, pattern, options):
                yield _format_line(filename, line_number, line, options)


def _line_matches(line, pattern, options):
    text = line

    if options.case_insensitive:
        text = text.lower()
        pattern = pattern.lower()

    if options.whole_line:
        matches = text.rstrip("\n") == pattern
    else:
        matches = pattern in text

    return matches != options.invert


def _format_line(filename, line_number, line, options):
    prefix = ""

    if options.include_filename:
        prefix += f"{filename}:"

    if options.include_line_number:
        prefix += f"{line_number}:"

    return prefix + line
