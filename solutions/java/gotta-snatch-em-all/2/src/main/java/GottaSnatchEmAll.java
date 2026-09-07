import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GottaSnatchEmAll {

    static Set<String> newCollection(List<String> cards) {
        return new HashSet<>(cards);
    }

    static boolean addCard(String card, Set<String> collection) {
        return collection.add(card);
    }

    static boolean canTrade(Set<String> myCollection, Set<String> theirCollection) {
        return myCollection.stream().anyMatch(card -> !theirCollection.contains(card))
                && theirCollection.stream().anyMatch(card -> !myCollection.contains(card));
    }

    static Set<String> commonCards(List<Set<String>> collections) {
        return collections.stream()
                .map(HashSet::new)
                .reduce((common, collection) -> {
                    common.retainAll(collection);
                    return common;
                })
                .orElseGet(HashSet::new);
    }

    static Set<String> allCards(List<Set<String>> collections) {
        return collections.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
