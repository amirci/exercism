import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GottaSnatchEmAll {

    static Set<String> newCollection(List<String> cards) {
        return new HashSet<>(cards);
    }

    static boolean addCard(String card, Set<String> collection) {
        return collection.add(card);
    }

    static boolean canTrade(Set<String> myCollection, Set<String> theirCollection) {
        return !myCollection.containsAll(theirCollection) && !theirCollection.containsAll(myCollection);
    }

    static Set<String> commonCards(List<Set<String>> collections) {
        var common = new HashSet<>(collections.getFirst());

        for (var collection : collections) {
            common.retainAll(collection);
        }

        return common;
    }

    static Set<String> allCards(List<Set<String>> collections) {
        var all = new HashSet<String>();

        for (var collection : collections) {
            all.addAll(collection);
        }

        return all;
    }
}
