class SqueakyClean {
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

        return switch (character) {
            case '4' -> 'a';
            case '3' -> 'e';
            case '0' -> 'o';
            case '1' -> 'l';
            case '7' -> 't';
            default -> replacementForLetter(character, capitalizeNext);
        };
    }

    private static Character replacementForLetter(char character, boolean capitalizeNext) {
        if (!Character.isLetter(character)) {
            return null;
        }

        return capitalizeNext ? Character.toUpperCase(character) : character;
    }
}
