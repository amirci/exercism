import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class BookStore {
    private static final double[] GROUP_PRICES = { 0.0, 8.0, 15.2, 21.6, 25.6, 30.0 };
    private static final int BOOK_TYPES = 5;

    double calculateBasketCost(List<Integer> books) {
        return cheapest(bookCounts(books), new HashMap<>());
    }

    private static double cheapest(int[] counts, Map<List<Integer>, Double> cache) {
        if (Arrays.stream(counts).allMatch(count -> count == 0)) {
            return 0.0;
        }

        var key = key(counts);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        var cheapest = nonEmptySubGroups(counts)
            .mapToDouble(group -> priceOf(group) + cheapest(removing(group, counts), cache))
            .min()
            .orElse(0.0);

        cache.put(key, cheapest);

        return cheapest;
    }

    private static int[] bookCounts(List<Integer> books) {
        var counts = new int[BOOK_TYPES];

        for (var book : books) {
            counts[book - 1]++;
        }

        return counts;
    }

    private static IntStream nonEmptySubGroups(int[] counts) {
        var availableBooks = availableBookIndexes(counts);
        var combinations = 1 << availableBooks.length;

        return IntStream.range(1, combinations)
            .mapToObj(mask -> groupFor(mask, availableBooks))
            .mapToInt(BookStore::groupMask);
    }

    private static int[] availableBookIndexes(int[] counts) {
        return IntStream.range(0, counts.length)
            .filter(index -> counts[index] > 0)
            .toArray();
    }

    private static int[] groupFor(int mask, int[] availableBooks) {
        return IntStream.range(0, availableBooks.length)
            .filter(bit -> includesBit(mask, bit))
            .map(bit -> availableBooks[bit])
            .toArray();
    }

    private static int groupMask(int[] group) {
        return Arrays.stream(group)
            .reduce(0, (mask, book) -> mask | (1 << book));
    }

    private static boolean includesBit(int mask, int bit) {
        return (mask & (1 << bit)) != 0;
    }

    private static int[] removing(int group, int[] counts) {
        var remaining = Arrays.copyOf(counts, counts.length);

        IntStream.range(0, BOOK_TYPES)
            .filter(book -> includesBit(group, book))
            .forEach(book -> remaining[book]--);

        return remaining;
    }

    private static double priceOf(int group) {
        return GROUP_PRICES[Integer.bitCount(group)];
    }

    private static List<Integer> key(int[] counts) {
        return Arrays.stream(counts)
            .boxed()
            .toList();
    }
}
