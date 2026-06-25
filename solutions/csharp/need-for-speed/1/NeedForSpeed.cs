class RemoteControlCar(int speed, int batteryDrain)
{
    private int _battery = 100;
    private int _distance;
    
    public bool BatteryDrained() => _battery < batteryDrain;

    public int DistanceDriven() => _distance;

    public void Drive()
    {
        if (BatteryDrained()) return;
        
        _battery -= batteryDrain;
        _distance += speed;
    }

    public static RemoteControlCar Nitro() => new(50, 4);
}

class RaceTrack(int distance)
{
    public bool TryFinishTrack(RemoteControlCar car)
    {
        while (!car.BatteryDrained() && car.DistanceDriven() < distance)
        {
            car.Drive();
        }

        return car.DistanceDriven() >= distance;
    }
}
