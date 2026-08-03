public class CarsAssemble {
    private static final double BASE_RATE = 221.0;

    public double productionRatePerHour(int speed) {
        return speed * BASE_RATE * successRate(speed);
    }

    public int workingItemsPerMinute(int speed) {
        return (int) (productionRatePerHour(speed) / 60);
    }

    private static double successRate(int speed) {
        return switch (speed) {
            case 10 -> 0.77;
            case 9 -> 0.8;
            case 5, 6, 7, 8 -> 0.9;
            default -> 1.0;
        };
    }
}
