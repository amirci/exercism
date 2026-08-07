import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class WordCount {
    private static final Pattern WORD = Pattern.compile("\\b\\w+(?:'\\w+)?\\b");

    public Map<String, Integer> phrase(String input) {
        return WORD.matcher(input.toLowerCase(Locale.ROOT))
            .results()
            .map(match -> match.group())
            .collect(Collectors.groupingBy(word -> word, Collectors.summingInt(word -> 1)));
    }
}
