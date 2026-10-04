import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

class Robot {
    private static final Random RANDOM = new Random();
    private static final NameRegistry NAMES = new NameRegistry();
    private String name;

    Robot() {
        reset();
    }

    String getName() {
        return name;
    }

    void reset() {
        name = NAMES.next();
    }

    private static String randomName() {
        return "%c%c%03d".formatted(randomLetter(), randomLetter(), RANDOM.nextInt(1000));
    }

    private static char randomLetter() {
        return (char) ('A' + RANDOM.nextInt(26));
    }

    private static class NameRegistry {
        private final Set<String> used = new HashSet<>();

        synchronized String next() {
            var candidate = Stream.generate(Robot::randomName)
                    .filter(Predicate.not(used::contains))
                    .findFirst()
                    .orElseThrow();

            used.add(candidate);
            return candidate;
        }
    }
}
