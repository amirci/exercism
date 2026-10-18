import java.util.List;
import java.util.Locale;

class Anagram {
    private final String normalizedTarget;
    private final String targetWord;

    public Anagram(String word) {
        targetWord = word;
        normalizedTarget = normalize(word);
    }

    public List<String> match(List<String> candidates) {
        return candidates.stream()
                .filter(candidate -> !targetWord.equalsIgnoreCase(candidate))
                .filter(candidate -> normalizedTarget.equals(normalize(candidate)))
                .toList();
    }

    private static String normalize(String word) {
        return word.toLowerCase(Locale.ROOT)
                .codePoints()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
