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

    int getSum() {
        Set<Integer> multiples = Arrays.stream(factors)
                .filter(factor -> factor != 0)
                .flatMap(factor -> IntStream.range(1, (limit - 1) / factor + 1)
                        .map(multiple -> factor * multiple))
                .boxed()
                .collect(Collectors.toSet());

        return multiples.stream().mapToInt(Integer::intValue).sum();
    }

}
