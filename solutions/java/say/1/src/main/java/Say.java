import java.util.ArrayList;

public class Say {
    private static final String[] SMALL = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen"
    };
    private static final String[] TENS = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };
    private static final long[] DIVISORS = {1_000_000_000L, 1_000_000L, 1_000L, 1};
    private static final String[] SCALES = {"billion", "million", "thousand", ""};

    public String say(long number) {
        if (number < 0 || number > 999_999_999_999L) {
            throw new IllegalArgumentException("Number out of range");
        }
        if (number == 0) {
            return SMALL[0];
        }

        var words = new ArrayList<String>();
        for (var i = 0; i < DIVISORS.length; i++) {
            var group = (int) (number / DIVISORS[i] % 1_000);
            if (group > 0) {
                words.add(underOneThousand(group));
                if (!SCALES[i].isEmpty()) {
                    words.add(SCALES[i]);
                }
            }
        }
        return String.join(" ", words);
    }

    private String underOneThousand(int number) {
        var words = new ArrayList<String>();
        if (number >= 100) {
            words.add(SMALL[number / 100]);
            words.add("hundred");
            number %= 100;
        }
        if (number >= 20) {
            var tens = TENS[number / 10];
            words.add(number % 10 == 0 ? tens : tens + "-" + SMALL[number % 10]);
        } else if (number > 0) {
            words.add(SMALL[number]);
        }
        return String.join(" ", words);
    }
}
