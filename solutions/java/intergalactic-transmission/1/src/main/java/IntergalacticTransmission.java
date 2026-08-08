import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntergalacticTransmission {
    private static final int DATA_BITS_PER_TRANSMISSION = 7;
    private static final int BITS_PER_BYTE = 8;

    public static List<Integer> getTransmitSequence(List<Integer> message) {
        var bits = message.stream()
            .flatMap(IntergalacticTransmission::toBits)
            .iterator();

        return transmit(bits);
    }

    public static List<Integer> decodeSequence(List<Integer> sequence) {
        return byteValues(dataBits(sequence));
    }

    private static int transmitByte(Iterator<Integer> bits) {
        var dataBits = new ArrayList<Integer>();

        for (var count = 0; count < DATA_BITS_PER_TRANSMISSION; count++) {
            dataBits.add(bits.hasNext() ? bits.next() : 0);
        }

        dataBits.add(parityBit(dataBits));

        return byteValue(dataBits);
    }

    private static List<Integer> transmit(Iterator<Integer> bits) {
        var sequence = new ArrayList<Integer>();

        while (bits.hasNext()) {
            sequence.add(transmitByte(bits));
        }

        return sequence;
    }

    private static List<Integer> dataBits(List<Integer> sequence) {
        return sequence.stream()
            .flatMap(IntergalacticTransmission::dataBits)
            .toList();
    }

    private static Stream<Integer> dataBits(int transmission) {
        ensureEvenParity(transmission);
        return toBits(transmission).limit(DATA_BITS_PER_TRANSMISSION);
    }

    private static List<Integer> byteValues(List<Integer> bits) {
        return IntStream.range(0, bits.size() / BITS_PER_BYTE)
            .map(group -> group * BITS_PER_BYTE)
            .mapToObj(start -> byteValue(bits.subList(start, start + BITS_PER_BYTE)))
            .toList();
    }

    private static Stream<Integer> toBits(int byteValue) {
        return IntStream.iterate(BITS_PER_BYTE - 1, bit -> bit - 1)
            .limit(BITS_PER_BYTE)
            .map(bit -> (byteValue >> bit) & 1)
            .boxed();
    }

    private static int byteValue(List<Integer> bits) {
        return bits.stream().reduce(0, (value, bit) -> (value << 1) | bit);
    }

    private static int parityBit(List<Integer> bits) {
        return bits.stream().mapToInt(Integer::intValue).sum() % 2;
    }

    private static void ensureEvenParity(int transmission) {
        if (Integer.bitCount(transmission) % 2 != 0) {
            throw new IllegalArgumentException();
        }
    }
}
