import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class RelativeDistance {
    private final Map<String, Set<String>> neighbors = new HashMap<>();

    RelativeDistance(Map<String, List<String>> familyTree) {
        familyTree.forEach((parent, children) -> {
            children.forEach(child -> connect(parent, child));

            connectSiblings(children);
        });
    }

    private void connect(String first, String second) {
        neighbors.computeIfAbsent(first, ignored -> new HashSet<>()).add(second);
        neighbors.computeIfAbsent(second, ignored -> new HashSet<>()).add(first);
    }

    private void connectSiblings(List<String> children) {
        for (int first = 0; first < children.size(); first++) {
            for (int second = first + 1; second < children.size(); second++) {
                connect(children.get(first), children.get(second));
            }
        }
    }

    int degreeOfSeparation(String personA, String personB) {
        if (!neighbors.containsKey(personA) || !neighbors.containsKey(personB)) {
            return -1;
        }

        var queue = new ArrayDeque<String>();
        var distances = new HashMap<String, Integer>();
        queue.add(personA);
        distances.put(personA, 0);

        while (!queue.isEmpty()) {
            String current = queue.remove();
            int distance = distances.get(current);

            if (current.equals(personB)) {
                return distance;
            }

            neighbors
                .get(current)
                .stream()
                .filter(neighbor -> !distances.containsKey(neighbor))
                .forEach(neighbor -> {
                        distances.put(neighbor, distance + 1);
                        queue.add(neighbor);
                });
        }

        return -1;
    }
}
