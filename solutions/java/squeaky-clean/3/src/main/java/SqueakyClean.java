import java.util.Map;

class SqueakyClean {
    private static final Map<Character, Character> LEETSPEAK = Map.of(
            '4', 'a',
            '3', 'e',
            '0', 'o',
            '1', 'l',
            '7', 't'
    );

    static String clean(String identifier) {
        var result = new StringBuilder();
        var capitalizeNext = false;

        for (var character : identifier.toCharArray()) {
            var replacement = replacementFor(character, capitalizeNext);

            if (replacement != null) {
                result.append(replacement);
            }

            capitalizeNext = character == '-';
        }

        return result.toString();
    }

    private static Character replacementFor(char character, boolean capitalizeNext) {
        if (Character.isWhitespace(character)) {
            return '_';
        }

        var leetReplacement = LEETSPEAK.get(character);
        if (leetReplacement != null) {
            return leetReplacement;
        }

        return replacementForLetter(character, capitalizeNext);
    }

    private static Character replacementForLetter(char character, boolean capitalizeNext) {
        if (!Character.isLetter(character)) {
            return null;
        }

        return capitalizeNext ? Character.toUpperCase(character) : character;
    }
}
