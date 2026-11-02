public class ExperimentalRemoteControlCar implements RemoteControlCar {
    private int distanceTravelled;

    @Override
    public void drive() {
        distanceTravelled += 20;
    }

    @Override
    public int getDistanceTravelled() {
        return distanceTravelled;
    }
}
