import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class GrepTool {

    String grep(String pattern, List<String> flags, List<String> files) {
        var strategy = SearchFiles.from(flags, files);

        return files.stream()
                .flatMap(file -> strategy.search(file, pattern))
                .collect(Collectors.joining("\n"));
    }

    private enum GrepOption {
        IGNORE_CASE("-i"),
        INVERT_MATCH("-v"),
        MATCH_ENTIRE_LINE("-x"),
        PRINT_LINE_NUMBER("-n"),
        FILES_ONLY("-l");

        private final String flag;

        GrepOption(String flag) {
            this.flag = flag;
        }

        static GrepOption parse(String flag) {
            return Stream.of(values())
                    .filter(option -> option.flag.equals(flag))
                    .findFirst()
                    .orElseThrow();
        }
    }

    @FunctionalInterface
    private interface SearchFile {
        Stream<String> search(String file, String pattern);
    }

    private static class SearchFiles {
        static SearchFile from(List<String> flags, List<String> files) {
            var options = asSet(flags);
            var matcher = MatchStrategy.from(options);
            var formatter = GrepFormatter.from(options, files);

            return options.contains(GrepOption.FILES_ONLY)
                    ? filesWithMatches(matcher)
                    : matchingLines(matcher, formatter);
        }

        private static EnumSet<GrepOption> asSet(List<String> flags) {
            return flags.stream()
                    .map(GrepOption::parse)
                    .collect(
                            () -> EnumSet.noneOf(GrepOption.class),
                            EnumSet::add,
                            EnumSet::addAll);
        }

        private static SearchFile matchingLines(MatchStrategy matcher, GrepFormatter formatter) {
            return (file, pattern) -> linesIn(file)
                    .filter(line -> matcher.matches(line.text(), pattern))
                    .map(formatter::format);
        }

        private static SearchFile filesWithMatches(MatchStrategy matcher) {
            return (file, pattern) -> linesIn(file).anyMatch(line -> matcher.matches(line.text(), pattern))
                    ? Stream.of(file)
                    : Stream.empty();
        }
    }

    @FunctionalInterface
    private interface MatchStrategy {
        boolean matches(String line, String pattern);

        static MatchStrategy from(EnumSet<GrepOption> options) {
            BiPredicate<String, String> matcher = options.contains(GrepOption.MATCH_ENTIRE_LINE)
                    ? String::equals
                    : String::contains;

            if (options.contains(GrepOption.IGNORE_CASE)) {
                matcher = ignoringCase(matcher);
            }

            if (options.contains(GrepOption.INVERT_MATCH)) {
                matcher = matcher.negate();
            }

            return matcher::test;
        }

        private static BiPredicate<String, String> ignoringCase(BiPredicate<String, String> matcher) {
            return (line, pattern) -> matcher.test(line.toLowerCase(), pattern.toLowerCase());
        }
    }

    @FunctionalInterface
    private interface GrepFormatter {
        String format(LineInfo line);

        static GrepFormatter from(EnumSet<GrepOption> options, List<String> files) {
            return line -> {
                var output = new StringBuilder();

                if (files.size() > 1) {
                    output.append(line.file()).append(":");
                }

                if (options.contains(GrepOption.PRINT_LINE_NUMBER)) {
                    output.append(line.number()).append(":");
                }

                return output.append(line.text()).toString();
            };
        }
    }

    private record LineInfo(String file, int number, String text) {
    }

    private static Stream<LineInfo> linesIn(String file) {
        try {
            var lines = Files.readAllLines(Path.of(file));

            return IntStream.range(0, lines.size())
                    .mapToObj(index -> new LineInfo(file, index + 1, lines.get(index)));
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
