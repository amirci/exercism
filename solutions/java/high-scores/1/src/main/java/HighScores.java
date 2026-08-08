
import java.util.Comparator;
import java.util.List;

class HighScores {

    private final List<Integer> scores;

    public HighScores(List<Integer> highScores) {
        scores = List.copyOf(highScores);
    }

    List<Integer> scores() {
        return scores;
    }

    Integer latest() {
        return scores.getLast();
    }

    Integer personalBest() {
        return scores.stream()
            .max(Comparator.naturalOrder())
            .orElseThrow();
    }

    List<Integer> personalTopThree() {
        return scores.stream()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .toList();
    }

}
