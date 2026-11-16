import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class Tournament {
    private static final String HEADER = "Team                           | MP |  W |  D |  L |  P\n";

    private final Map<String, TeamStats> teams = new HashMap<>();

    String printTable() {
        return HEADER + teams
            .values()
            .stream()
            .sorted(teamRankingOrder())
            .map(TeamStats::formatRow)
            .collect(Collectors.joining());
    }

    private static Comparator<TeamStats> teamRankingOrder() {
        return Comparator.comparingInt((TeamStats team) -> team.points).reversed()
                .thenComparing(team -> team.name);
    }

    void applyResults(String resultString) {
        resultString.lines()
                .filter(row -> !row.isBlank())
                .forEach(this::applyResult);
    }

    private TeamStats findTeam(String teamName) {
        return teams.computeIfAbsent(teamName, TeamStats::new);
    }

    private void applyResult(String row) {
        var parts = row.split(";");

        var first = findTeam(parts[0]);
        first.matchesPlayed++;

        var second = findTeam(parts[1]);
        second.matchesPlayed++;

        switch (parts[2]) {
            case "win" -> registerWin(first, second);
            case "draw" -> registerDraw(first, second);
            case "loss" -> registerWin(second, first);
        }
    }

    private void registerWin(TeamStats winner, TeamStats loser) {
        winner.wins++;
        winner.points += 3;
        loser.losses++;
    }

    private void registerDraw(TeamStats first, TeamStats second) {
        first.draws++;
        first.points++;
        second.draws++;
        second.points++;
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
