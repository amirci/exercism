import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Say {
    private static final String[] SMALL = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen"
    };
    private static final String[] TENS = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };
    private static final String[] SCALES = {"", "thousand", "million", "billion"};

    public String say(long number) {
        validateNumberRangeUpTo999Billion(number);

        if (number == 0) {
            return SMALL[0];
        }

        return splitIntoThousands(number).stream()
                .filter(Predicate.not(Group::isEmpty))
                .map(this::wordsForGroup)
                .collect(Collectors.joining(" "));
    }

    private List<Group> splitIntoThousands(long number) {
        var groups = new ArrayList<Group>();
        var scale = 0;

        while (number > 0) {
            groups.add(new Group((int) (number % 1_000), SCALES[scale]));
            number /= 1_000;
            scale++;
        }

        Collections.reverse(groups);
        return groups;
    }

    private String wordsForGroup(Group group) {
        return group.withScale(groupToWords(group));
    }

    private void validateNumberRangeUpTo999Billion(long number) {
        if (number < 0 || number > 999_999_999_999L) {
            throw new IllegalArgumentException("Number out of range");
        }
    }

    private String groupToWords(Group group) {
        var number = group.value();
        return Stream
            .concat(hundredsPart(number), belowOneHundred(number % 100))
            .collect(Collectors.joining(" "));
    }

    private Stream<String> hundredsPart(int number) {
        if (number < 100) {
            return Stream.empty();
        }

        return Stream.of(SMALL[number / 100], "hundred");
    }

    private Stream<String> belowOneHundred(int number) {
        if (number == 0) {
            return Stream.empty();
        }
        if (number < 20) {
            return Stream.of(SMALL[number]);
        }

        var tens = TENS[number / 10];
        var words = number % 10 == 0 ? tens : tens + "-" + SMALL[number % 10];
        return Stream.of(words);
    }

    private record Group(int value, String scale) {
        private boolean isEmpty() {
            return value == 0;
        }

        private boolean hasScale() {
            return !scale.isEmpty();
        }

        public String withScale(String words) {
            return hasScale() ? words + " " + scale : words;
        }
    }
}
