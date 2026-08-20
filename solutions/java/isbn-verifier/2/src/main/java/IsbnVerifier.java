import java.util.regex.Pattern;
import java.util.stream.IntStream;

class IsbnVerifier {
    private static final Pattern ISBN_PATTERN = Pattern.compile("\\d{9}[\\dX]");

    boolean isValid(String stringToVerify) {
        var isbn = stringToVerify.replace("-", "");

        return ISBN_PATTERN.matcher(isbn).matches() && checksum(isbn) % 11 == 0;
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
