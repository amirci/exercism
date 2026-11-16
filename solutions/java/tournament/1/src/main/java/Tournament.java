import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class Tournament {
    private static final String HEADER = "Team                           | MP |  W |  D |  L |  P\n";

    private final Map<String, TeamStats> teams = new HashMap<>();

    String printTable() {
        var table = new StringBuilder(HEADER);
        teams.values().stream()
                .sorted(Comparator.comparingInt((TeamStats team) -> team.points).reversed()
                        .thenComparing(team -> team.name))
                .forEach(team -> table.append(team.formatRow()));
        return table.toString();
    }

    void applyResults(String resultString) {
        for (String row : resultString.split("\\n")) {
            if (row.isBlank()) {
                continue;
            }

            String[] parts = row.split(";");
            TeamStats first = teams.computeIfAbsent(parts[0], TeamStats::new);
            TeamStats second = teams.computeIfAbsent(parts[1], TeamStats::new);
            first.matchesPlayed++;
            second.matchesPlayed++;

            switch (parts[2]) {
                case "win" -> {
                    first.wins++;
                    first.points += 3;
                    second.losses++;
                }
                case "draw" -> {
                    first.draws++;
                    first.points++;
                    second.draws++;
                    second.points++;
                }
                case "loss" -> {
                    first.losses++;
                    second.wins++;
                    second.points += 3;
                }
            }
        }
    }

    private static class TeamStats {
        private final String name;
        private int matchesPlayed;
        private int wins;
        private int draws;
        private int losses;
        private int points;

        private TeamStats(String name) {
            this.name = name;
        }

        private String formatRow() {
            return String.format("%-30s | %2d | %2d | %2d | %2d | %2d%n",
                    name, matchesPlayed, wins, draws, losses, points);
        }
    }
}
