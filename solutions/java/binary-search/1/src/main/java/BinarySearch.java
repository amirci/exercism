import java.util.List;

class BinarySearch {
    private final List<Integer> items;

    BinarySearch(List<Integer> items) {
        this.items = List.copyOf(items);
    }

    int indexOf(int item) throws ValueNotFoundException {
        int left = 0;
        int right = items.size() - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int value = items.get(middle);

            if (value == item) {
                return middle;
            }
            if (value < item) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        throw new ValueNotFoundException("Value not in array");
    }
}
