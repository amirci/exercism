import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class CustomSet<T> {
    private final Set<T> elements;

    CustomSet() {
        elements = new HashSet<>();
    }

    CustomSet(Collection<T> data) {
        elements = new HashSet<>(data);
    }

    boolean isEmpty() {
        return elements.isEmpty();
    }

    boolean contains(T element) {
        return elements.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        return elements.stream().noneMatch(other::contains);
    }

    boolean add(T element) {
        return elements.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CustomSet<?> other && elements.equals(other.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        return new CustomSet<>(elements.stream().filter(other::contains).toList());
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        var union = new CustomSet<>(elements);
        union.elements.addAll(other.elements);
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        return new CustomSet<>(elements.stream().filter(element -> !other.contains(element)).toList());
    }

    boolean isSubset(CustomSet<T> other) {
        return elements.containsAll(other.elements);
    }
}
