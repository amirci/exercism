import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SumOfMultiples {

    private final int limit;
    private final int[] factors;

    SumOfMultiples(int limit, int[] factors) {
        this.limit = limit;
        this.factors = factors;
    }

    private IntStream multiplesFor(int factor) {
        return IntStream
            .rangeClosed(1, (limit - 1) / factor)
            .map(multiple -> factor * multiple);
    }

    int getSum() {
        return Arrays
            .stream(factors)
            .filter(factor -> factor != 0)
            .flatMap(this::multiplesFor)
            .boxed()
            .collect(Collectors.toSet())
            .stream().mapToInt(Integer::intValue).sum();
    }

}
