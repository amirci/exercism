import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

class RailFenceCipher {
    private final int rows;

    RailFenceCipher(int rows) {
        this.rows = rows;
    }

    String getEncryptedData(String message) {
        return zigZagIndexes(message.length())
            .mapToObj(index -> String.valueOf(message.charAt(index)))
            .collect(Collectors.joining());
    }

    String getDecryptedData(String message) {
        var decoded = new char[message.length()];
        var encodedIndex = 0;

        for (var targetIndex : zigZagIndexes(message.length()).toArray()) {
            decoded[targetIndex] = message.charAt(encodedIndex);
            encodedIndex++;
        }

        return new String(decoded);
    }

    private IntStream zigZagIndexes(int length) {
        return IntStream.range(0, length)
            .boxed()
            .sorted(Comparator.comparingInt(this::targetRail))
            .mapToInt(Integer::intValue);
    }

    private int targetRail(int index) {
        var period = 2 * (rows - 1);
        var position = index % period;

        return position < rows ? position : period - position;
    }
}
