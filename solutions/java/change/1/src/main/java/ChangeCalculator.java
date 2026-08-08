import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        coins = currencyCoins.stream()
            .sorted()
            .toList();
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative totals are not allowed.");
        }

        var best = bestChanges(grandTotal);

        if (best[grandTotal] == null) {
            throw new IllegalArgumentException(
                "The total " + grandTotal + " cannot be represented in the given currency."
            );
        }

        return best[grandTotal].stream()
            .sorted()
            .toList();
    }

    @SuppressWarnings("unchecked")
    private List<Integer>[] bestChanges(int grandTotal) {
        var best = new List[grandTotal + 1];
        best[0] = List.<Integer>of();

        for (var total = 1; total <= grandTotal; total++) {
            final var currentTotal = total;
            best[total] = coins.stream()
                .filter(coin -> coin <= currentTotal)
                .map(coin -> changeWith(best[currentTotal - coin], coin))
                .filter(change -> change != null)
                .min(Comparator.comparingInt(List::size))
                .orElse(null);
        }

        return best;
    }

    private static List<Integer> changeWith(List<Integer> previousChange, int coin) {
        if (previousChange == null) {
            return null;
        }

        var change = new ArrayList<>(previousChange);
        change.add(coin);

        return change;
    }
}
