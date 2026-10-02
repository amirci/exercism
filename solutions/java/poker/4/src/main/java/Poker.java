import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class Poker {
    private final List<String> hands;

    Poker(List<String> hands) {
        this.hands = List.copyOf(hands);
    }

    List<String> getBestHands() {
        var parsedHands = hands.stream().map(Hand::parse).toList();

        var best = parsedHands.stream()
                .map(Hand::key)
                .max(Poker::compareScores)
                .orElseThrow();

        return parsedHands.stream()
                .filter(hand -> hand.key().equals(best))
                .map(Hand::toString)
                .toList();
    }

    private static int compareScores(Score first, Score second) {
        int categoryComparison = Integer.compare(first.category().strength, second.category().strength);

        if (categoryComparison != 0) {
            return categoryComparison;
        }

        for (int index = 0; index < Math.min(first.tieBreakers().size(), second.tieBreakers().size()); index++) {
            int comparison = Integer.compare(first.tieBreakers().get(index), second.tieBreakers().get(index));
            if (comparison != 0) {
                return comparison;
            }
        }
        return Integer.compare(first.tieBreakers().size(), second.tieBreakers().size());
    }

    private record Score(HandCategory category, List<Integer> tieBreakers) {
        private Score {
            tieBreakers = List.copyOf(tieBreakers);
        }
    }

    private record RankGroup(int rank, int count) {}

    private enum HandCategory {
        STRAIGHT_FLUSH(8,
                hand -> hand.flush && hand.straightHigh > 0,
                hand -> List.of(hand.straightHigh)),
        FOUR_OF_A_KIND(7,
                hand -> hand.mostFrequentCount() == 4,
                hand -> List.of(hand.mostFrequentRank(), hand.secondRank())),
        FULL_HOUSE(6,
                hand -> hand.mostFrequentCount() == 3 && hand.secondMostFrequentCount() == 2,
                hand -> List.of(hand.mostFrequentRank(), hand.secondRank())),
        FLUSH(5,
                hand -> hand.flush,
                hand -> hand.ranks),
        STRAIGHT(4,
                hand -> hand.straightHigh > 0,
                hand -> List.of(hand.straightHigh)),
        THREE_OF_A_KIND(3,
                hand -> hand.mostFrequentCount() == 3,
                hand -> hand.tieBreakersFor(3)),
        TWO_PAIR(2,
                hand -> hand.mostFrequentCount() == 2 && hand.secondMostFrequentCount() == 2,
                hand -> hand.tieBreakersFor(2)),
        ONE_PAIR(1,
                hand -> hand.mostFrequentCount() == 2 && hand.secondMostFrequentCount() != 2,
                hand -> hand.tieBreakersFor(2)),
        HIGH_CARD(0,
                _ -> true,
                hand -> hand.ranks);

        private final int strength;
        private final Predicate<Hand> matches;
        private final Function<Hand, List<Integer>> tieBreakers;

        HandCategory(int strength, Predicate<Hand> matches, Function<Hand, List<Integer>> tieBreakers) {
            this.strength = strength;
            this.matches = matches;
            this.tieBreakers = tieBreakers;
        }

        Optional<Score> score(Hand hand) {
            return matches.test(hand)
                    ? Optional.of(new Score(this, tieBreakers.apply(hand)))
                    : Optional.empty();
        }
    }

    private record Card(int rank, char suit) {
        static Card parse(String text) {
            var rankText = text.substring(0, text.length() - 1);
            var suit = text.charAt(text.length() - 1);
            var rank = switch (rankText) {
                case "J" -> 11;
                case "Q" -> 12;
                case "K" -> 13;
                case "A" -> 14;
                default -> Integer.parseInt(rankText);
            };
            return new Card(rank, suit);
        }
    }

        private record Hand(
            String raw,
            List<Integer> ranks,
            List<RankGroup> frequencies,
            boolean flush,
            int straightHigh,
            Score key) {

        static Hand parse(String raw) {
            var cards = fromString(raw);
            var ranks = cardsByRank(cards);
            var counts = frequencies(cards);
            var frequencyGroups = frequencies(counts);
            var flush = isFlush(cards);
            var straightHigh = straightHigh(ranks);
            var key = rankingKey(ranks, frequencyGroups, flush, straightHigh);
            return new Hand(raw, ranks, frequencyGroups, flush, straightHigh, key);
        }

        @Override
        public String toString() {
            return raw;
        }

        private static boolean isFlush(List<Card> cards) {
            return cards.stream().map(Card::suit).distinct().count() == 1;
        }

        private static HashMap<Integer, Integer> frequencies(List<Card> cards) {
            var counts = new HashMap<Integer, Integer>();
            cards.forEach(card -> counts.merge(card.rank(), 1, Integer::sum));
            return counts;
        }

        private static List<Integer> cardsByRank(List<Card> cards) {
            return cards.stream().map(Card::rank).sorted(Comparator.reverseOrder()).toList();
        }

        private static List<Card> fromString(String raw) {
            return Stream.of(raw.split(" ")).map(Card::parse).toList();
        }

        private static Score rankingKey(
                List<Integer> ranks,
                List<RankGroup> frequencies,
                boolean flush,
                int straightHigh) {
            var hand = new Hand("", ranks, frequencies, flush, straightHigh, null);
            return Stream.of(
                            HandCategory.STRAIGHT_FLUSH,
                            HandCategory.FOUR_OF_A_KIND,
                            HandCategory.FULL_HOUSE,
                            HandCategory.FLUSH,
                            HandCategory.STRAIGHT,
                            HandCategory.THREE_OF_A_KIND,
                            HandCategory.TWO_PAIR,
                            HandCategory.ONE_PAIR,
                            HandCategory.HIGH_CARD)
                    .map(category -> category.score(hand))
                    .flatMap(Optional::stream)
                    .findFirst()
                    .orElseThrow();
        }

        private static List<RankGroup> frequencies(Map<Integer, Integer> counts) {
            return counts.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                    .map(entry -> new RankGroup(entry.getKey(), entry.getValue()))
                    .toList();
        }

        private int mostFrequentCount() {
            return frequencies.getFirst().count();
        }

        private int mostFrequentRank() {
            return frequencies.getFirst().rank();
        }

        private int secondRank() {
            return frequencies.get(1).rank();
        }

        private int secondMostFrequentCount() {
            return frequencies.get(1).count();
        }

        private List<Integer> tieBreakersFor(int groupSize) {
            var grouped = frequencies.stream()
                    .filter(group -> group.count() == groupSize)
                    .map(RankGroup::rank)
                    .toList();
            var kickers = ranks.stream().filter(rank -> !grouped.contains(rank)).toList();
            var result = new ArrayList<>(grouped);
            result.addAll(kickers);
            return result;
        }

        private static int straightHigh(List<Integer> ranks) {
            var unique = ranks.stream().distinct().sorted().toList();
            if (unique.size() != 5) return 0;
            if (unique.equals(List.of(2, 3, 4, 5, 14))) return 5;
            return unique.get(4) - unique.get(0) == 4 ? unique.get(4) : 0;
        }
    }
}
