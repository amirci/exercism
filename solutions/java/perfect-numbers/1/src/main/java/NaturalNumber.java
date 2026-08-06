import java.util.stream.IntStream;

class NaturalNumber {
    private final int number;

    NaturalNumber(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("You must supply a natural number (positive integer)");
        }

        this.number = number;
    }

    Classification getClassification() {
        return switch (Integer.compare(aliquotSum(), number)) {
            case -1 -> Classification.DEFICIENT;
            case 1 -> Classification.ABUNDANT;
            default -> Classification.PERFECT;
        };
    }

    private int aliquotSum() {
        if (number == 1) {
            return 0;
        }

        return 1 + IntStream.rangeClosed(2, (int) Math.sqrt(number))
            .filter(factor -> number % factor == 0)
            .flatMap(this::factorPair)
            .sum();
    }

    private IntStream factorPair(int factor) {
        var pair = number / factor;

        return pair == factor ? IntStream.of(factor) : IntStream.of(factor, pair);
    }
}
