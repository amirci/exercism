import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class DnDCharacter {
    private static final int DICE_PER_ABILITY = 4;
    private static final int SIDES_PER_DIE = 6;
    private static final int BASE_HITPOINTS = 10;
    private static final Random RANDOM = new Random();

    private final int strength = rollAbility();
    private final int dexterity = rollAbility();
    private final int constitution = rollAbility();
    private final int intelligence = rollAbility();
    private final int wisdom = rollAbility();
    private final int charisma = rollAbility();

    private int rollAbility() {
        return ability(rollDice());
    }

    int ability(List<Integer> scores) {
        return scores
            .stream()
            .sorted((left, right) -> right - left)
            .limit(3)
            .mapToInt(Integer::intValue)
            .sum();
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
