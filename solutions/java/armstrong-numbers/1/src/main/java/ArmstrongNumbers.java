import java.util.ArrayList;
import java.util.List;

class ArmstrongNumbers {

    boolean isArmstrongNumber(int numberToCheck) {
        var digits = digits(numberToCheck);
        var exponent = digits.size();
        var sum = digits.stream().mapToInt(digit -> pow(digit, exponent)).sum();

        return sum == numberToCheck;
    }

    private static List<Integer> digits(int number) {
        var digits = new ArrayList<Integer>();
        var remaining = number;

        if (remaining == 0) {
            digits.add(0);
        }

        while (remaining > 0) {
            digits.add(remaining % 10);
            remaining /= 10;
        }

        return digits;
    }

    private static int pow(int number, int exponent) {
        var result = 1;

        for (var i = 0; i < exponent; i++) {
            result *= number;
        }

        return result;
    }
}
