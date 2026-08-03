import java.util.Locale;
import java.util.regex.Pattern;

public class LogLevels {
    private static final Pattern LOG_LINE = Pattern.compile("\\[(\\w+)]:(.*)", Pattern.DOTALL);

    public static String message(String logLine) {
        return parse(logLine).message();
    }

    public static String logLevel(String logLine) {
        return parse(logLine).level();
    }

    public static String reformat(String logLine) {
        var log = parse(logLine);

        return log.message() + " (" + log.level() + ")";
    }

    private static LogLine parse(String logLine) {
        var match = LOG_LINE.matcher(logLine);

        if (!match.matches()) {
            throw new IllegalArgumentException();
        }

        return new LogLine(match.group(1).toLowerCase(Locale.ROOT), match.group(2).trim());
    }

    private record LogLine(String level, String message) {}
}
