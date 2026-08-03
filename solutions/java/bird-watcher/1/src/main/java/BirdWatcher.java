
import java.util.Arrays;
import java.util.stream.IntStream;

class BirdWatcher {
    private static final int[] LAST_WEEK = {0, 2, 5, 3, 7, 8, 4};
    private static final int BUSY_DAY_THRESHOLD = 5;

    private final int[] birdsPerDay;

    public BirdWatcher(int[] birdsPerDay) {
        this.birdsPerDay = birdsPerDay.clone();
    }

    public static int[] getLastWeek() {
        return LAST_WEEK.clone();
    }

    public int getToday() {
        return birdsPerDay[birdsPerDay.length - 1];
    }

    public void incrementTodaysCount() {
        birdsPerDay[birdsPerDay.length - 1]++;
    }

    public boolean hasDayWithoutBirds() {
        return birds().anyMatch(count -> count == 0);
    }

    public int getCountForFirstDays(int numberOfDays) {
        return birds().limit(numberOfDays).sum();
    }

    public int getBusyDays() {
        return (int) birds().filter(count -> count >= BUSY_DAY_THRESHOLD).count();
    }

    private IntStream birds() {
        return Arrays.stream(birdsPerDay);
    }
}
