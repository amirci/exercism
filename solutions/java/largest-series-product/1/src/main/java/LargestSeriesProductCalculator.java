import java.util.stream.IntStream;

class LargestSeriesProductCalculator {
    private final String inputNumber;

    LargestSeriesProductCalculator(String inputNumber) {
        if (!inputNumber.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("String to search may only contain digits.");
        }

        this.inputNumber = inputNumber;
    }

    long calculateLargestProductForSeriesLength(int numberOfDigits) {
        validateSeriesLength(numberOfDigits);

        var digits = digits();

        return windowStarts(digits, numberOfDigits)
            .mapToLong(start -> product(digits, start, numberOfDigits))
            .max()
            .orElse(1);
    }

    private void validateSeriesLength(int numberOfDigits) {
        if (numberOfDigits > inputNumber.length()) {
            throw new IllegalArgumentException(
                "Series length must be less than or equal to the length of the string to search."
            );
        }
    }

    private int[] digits() {
        return inputNumber.chars()
            .map(digit -> digit - '0')
            .toArray();
    }

    private IntStream windowStarts(int[] digits, int length) {
        return IntStream.rangeClosed(0, digits.length - length);
    }

    private long product(int[] digits, int start, int length) {
        return IntStream.range(start, start + length)
            .mapToLong(index -> digits[index])
            .reduce(1L, (product, digit) -> product * digit);
    }
}
