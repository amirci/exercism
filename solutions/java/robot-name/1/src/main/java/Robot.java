import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Robot {
    private static final Random RANDOM = new Random();
    private static final Set<String> USED_NAMES = new HashSet<>();
    private String name;

    Robot() {
        reset();
    }

    String getName() {
        return name;
    }

    void reset() {
        do {
            name = randomName();
        } while (!reserveName(name));
    }

    private static String randomName() {
        char first = (char) ('A' + RANDOM.nextInt(26));
        char second = (char) ('A' + RANDOM.nextInt(26));
        int number = RANDOM.nextInt(1000);
        return String.format("%c%c%03d", first, second, number);
    }

    private static synchronized boolean reserveName(String name) {
        return USED_NAMES.add(name);
    }
}
