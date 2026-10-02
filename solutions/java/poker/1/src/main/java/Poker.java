import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                .map(Hand::raw)
                .toList();
    }

    private static int compareScores(Score first, Score second) {
        int categoryComparison = Integer.compare(
                first.category().strength,
                second.category().strength);
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

    private record Score(HandCategory category, List<Integer> tieBreakers) {}

    private enum HandCategory {
        HIGH_CARD(0) {
            @Override
            Optional<Score> score(Hand hand) {
                return Optional.of(new Score(this, hand.ranks));
            }
        },
        ONE_PAIR(1) {
            @Override
            Optional<Score> score(Hand hand) {
                var frequencies = hand.frequencies;
                return Hand.fromPredicate(
                        frequencies.getFirst().getValue() == 2,
                        new Score(this, Hand.orderedRanks(frequencies, 2, hand.ranks)));
            }
        },
        TWO_PAIR(2) {
            @Override
            Optional<Score> score(Hand hand) {
                var frequencies = hand.frequencies;
                return Hand.fromPredicate(
                        frequencies.get(0).getValue() == 2 && frequencies.get(1).getValue() == 2,
                        new Score(this, Hand.orderedRanks(frequencies, 2, hand.ranks)));
            }
        },
        THREE_OF_A_KIND(3) {
            @Override
            Optional<Score> score(Hand hand) {
                var frequencies = hand.frequencies;
                return Hand.fromPredicate(
                        frequencies.getFirst().getValue() == 3,
                        new Score(this, Hand.orderedRanks(frequencies, 3, hand.ranks)));
            }
        },
        STRAIGHT(4) {
            @Override
            Optional<Score> score(Hand hand) {
                return Hand.fromPredicate(
                        hand.straightHigh > 0,
                        new Score(this, List.of(hand.straightHigh)));
            }
        },
        FLUSH(5) {
            @Override
            Optional<Score> score(Hand hand) {
                return Hand.fromPredicate(hand.flush, new Score(this, hand.ranks));
            }
        },
        FULL_HOUSE(6) {
            @Override
            Optional<Score> score(Hand hand) {
                var frequencies = hand.frequencies;
                return Hand.fromPredicate(
                        frequencies.get(0).getValue() == 3 && frequencies.get(1).getValue() == 2,
                        new Score(this, List.of(frequencies.get(0).getKey(), frequencies.get(1).getKey())));
            }
        },
        FOUR_OF_A_KIND(7) {
            @Override
            Optional<Score> score(Hand hand) {
                var frequencies = hand.frequencies;
                return Hand.fromPredicate(
                        frequencies.get(0).getValue() == 4,
                        new Score(this, List.of(frequencies.get(0).getKey(), frequencies.get(1).getKey())));
            }
        },
        STRAIGHT_FLUSH(8) {
            @Override
            Optional<Score> score(Hand hand) {
                return Hand.fromPredicate(
                        hand.flush && hand.straightHigh > 0,
                        new Score(this, List.of(hand.straightHigh)));
            }
        };

        private final int strength;

        HandCategory(int strength) {
            this.strength = strength;
        }

        abstract Optional<Score> score(Hand hand);
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
            List<Card> cards,
            List<Integer> ranks,
            List<Map.Entry<Integer, Integer>> frequencies,
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
            return new Hand(raw, List.copyOf(cards), ranks, frequencyGroups, flush, straightHigh, key);
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
                List<Map.Entry<Integer, Integer>> frequencies,
                boolean flush,
                int straightHigh) {
            var hand = new Hand("", List.of(), ranks, frequencies, flush, straightHigh, null);
            return Stream.of(HandCategory.values())
                    .sorted(Comparator.comparingInt(category -> -category.strength))
                    .map(category -> category.score(hand))
                    .flatMap(Optional::stream)
                    .findFirst()
                    .orElseThrow();
        }

        private static <T> Optional<T> fromPredicate(boolean flag, T value) {
            return flag ? Optional.of(value) : Optional.empty();
        }

        private static List<Map.Entry<Integer, Integer>> frequencies(Map<Integer, Integer> counts) {
            return counts.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                    .toList();
        }

        private static List<Integer> orderedRanks(
                List<Map.Entry<Integer, Integer>> frequencies,
                int groupSize,
                List<Integer> ranks) {
            var grouped = frequencies.stream()
                    .filter(entry -> entry.getValue() == groupSize)
                    .map(Map.Entry::getKey)
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
