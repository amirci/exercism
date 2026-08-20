import java.util.stream.IntStream;

class IsbnVerifier {

    boolean isValid(String stringToVerify) {
        var isbn = stringToVerify.replace("-", "");

        return hasValidCharacters(isbn) && checksum(isbn) % 11 == 0;
    }

    private static boolean hasValidCharacters(String isbn) {
        return isbn.length() == 10
                && isbn.substring(0, 9).chars().allMatch(Character::isDigit)
                && isValidCheckDigit(isbn.charAt(9));
    }

    private static boolean isValidCheckDigit(char character) {
        return Character.isDigit(character) || character == 'X';
    }

    private static int checksum(String isbn) {
        return IntStream.range(0, isbn.length())
                .map(index -> digitValue(isbn.charAt(index)) * (10 - index))
                .sum();
    }

    private static int digitValue(char character) {
        return character == 'X' ? 10 : Character.digit(character, 10);
    }
}
