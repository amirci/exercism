import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private final Set<Node> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();
    private final Map<String, String> attributes;

    public Graph() {
        this(Map.of());
    }

    public Graph(Map<String, String> attributes) {
        this.attributes = Map.copyOf(attributes);
    }

    public Collection<Node> getNodes() {
        return Set.copyOf(nodes);
    }

    public Collection<Edge> getEdges() {
        return Set.copyOf(edges);
    }

    public Graph node(String name) {
        return node(name, Map.of());
    }

    public Graph node(String name, Map<String, String> attributes) {
        nodes.add(new Node(name, attributes));
        return this;
    }

    public Graph edge(String start, String end) {
        return edge(start, end, Map.of());
    }

    public Graph edge(String start, String end, Map<String, String> attributes) {
        edges.add(new Edge(start, end, attributes));
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
