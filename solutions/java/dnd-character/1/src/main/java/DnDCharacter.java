import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class DnDCharacter {
    private static final int DICE_PER_ABILITY = 4;
    private static final int SIDES_PER_DIE = 6;
    private static final int BASE_HITPOINTS = 10;
    private static final Random RANDOM = new Random();

    private final int strength = ability(rollDice());
    private final int dexterity = ability(rollDice());
    private final int constitution = ability(rollDice());
    private final int intelligence = ability(rollDice());
    private final int wisdom = ability(rollDice());
    private final int charisma = ability(rollDice());

    int ability(List<Integer> scores) {
        return threeLargestValues(scores).sum();
    }

    private IntStream threeLargestValues(List<Integer> scores) {
        return scores.stream()
            .sorted((left, right) -> right - left)
            .limit(3)
            .mapToInt(Integer::intValue);
    }

    List<Integer> rollDice() {
        return IntStream.generate(() -> RANDOM.nextInt(SIDES_PER_DIE) + 1)
                .limit(DICE_PER_ABILITY)
                .boxed()
                .toList();
    }

    int modifier(int input) {
        return Math.floorDiv(input - 10, 2);
    }

    int getStrength() {
        return strength;
    }

    int getDexterity() {
        return dexterity;
    }

    int getConstitution() {
        return constitution;
    }

    int getIntelligence() {
        return intelligence;
    }

    int getWisdom() {
        return wisdom;
    }

    int getCharisma() {
        return charisma;
    }

    int getHitpoints() {
        return BASE_HITPOINTS + modifier(constitution);
    }
}
