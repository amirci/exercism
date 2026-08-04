class NeedForSpeed {
    private static final int INITIAL_BATTERY = 100;
    private static final int NITRO_SPEED = 50;
    private static final int NITRO_BATTERY_DRAIN = 4;

    private final int speed;
    private final int batteryDrain;
    private int battery = INITIAL_BATTERY;
    private int distance;

    NeedForSpeed(int speed, int batteryDrain) {
        this.speed = speed;
        this.batteryDrain = batteryDrain;
    }

    public boolean batteryDrained() {
        return battery < batteryDrain;
    }

    public int distanceDriven() {
        return distance;
    }

    public void drive() {
        if (batteryDrained()) {
            return;
        }

        battery -= batteryDrain;
        distance += speed;
    }

    public static NeedForSpeed nitro() {
        return new NeedForSpeed(NITRO_SPEED, NITRO_BATTERY_DRAIN);
    }

    int maximumDistance() {
        return speed * (INITIAL_BATTERY / batteryDrain);
    }
}

class RaceTrack {
    private final int distance;

    RaceTrack(int distance) {
        this.distance = distance;
    }

    public boolean canFinishRace(NeedForSpeed car) {
        return car.maximumDistance() >= distance;
    }
}
