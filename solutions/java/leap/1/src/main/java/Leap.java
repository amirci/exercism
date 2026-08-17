class Leap {

    boolean isLeapYear(int year) {
        return isMultipleOf(year, 4)
                && (!isMultipleOf(year, 100) || isMultipleOf(year, 400));
    }

    private boolean isMultipleOf(int year, int factor) {
        return year % factor == 0;
    }

}
