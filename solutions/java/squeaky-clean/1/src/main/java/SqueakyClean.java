import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SqueakyClean {
    private static final Pattern CLEANER_PATTERN = Pattern.compile(
            "(?<ws>\\s)|-(?<kb>\\p{L})|(?<leet>[43017])|([^\\p{L}_]|[α-ω])"
    );

    static String clean(String identifier) {
        var matcher = CLEANER_PATTERN.matcher(identifier);
        var result = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacementFor(matcher)));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    private static String replacementFor(Matcher match) {
        if (match.group("ws") != null) {
            return "_";
        }

        if (match.group("kb") != null) {
            return match.group("kb").toUpperCase(Locale.ROOT);
        }

        if (match.group("leet") != null) {
            return leetReplacement(match.group("leet").charAt(0));
        }

        return "";
    }

    private static String leetReplacement(char character) {
        return switch (character) {
            case '4' -> "a";
            case '3' -> "e";
            case '0' -> "o";
            case '1' -> "l";
            case '7' -> "t";
            default -> "";
        };
    }
}
