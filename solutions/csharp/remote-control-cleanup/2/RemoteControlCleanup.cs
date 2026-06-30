public class RemoteControlCar
{
    public string CurrentSponsor { get; private set; } = string.Empty;

    private Speed currentSpeed;

    public CarTelemetry Telemetry { get; }

    public RemoteControlCar()
    {
        Telemetry = new CarTelemetry(this);
    }

    public string GetSpeed() => currentSpeed.ToString();

    public class CarTelemetry(RemoteControlCar car)
    {

        public void Calibrate()
        {

        }

        public bool SelfTest()
        {
            return true;
        }

        public void ShowSponsor(string sponsorName)
        {
            car.SetSponsor(sponsorName);
        }

        public void SetSpeed(decimal amount, string unitsString)
        {
            var speedUnits = unitsString switch
            {
                "cps" => SpeedUnits.CentimetersPerSecond,
                _ => SpeedUnits.MetersPerSecond
            };

            car.SetSpeed(new Speed(amount, speedUnits));
        }
    }

    private void SetSponsor(string sponsorName)
    {
        CurrentSponsor = sponsorName;
    }

    private void SetSpeed(Speed speed)
    {
        currentSpeed = speed;
    }

    private enum SpeedUnits
    {
        MetersPerSecond,
        CentimetersPerSecond
    }

    private struct Speed
    {
        public decimal Amount { get; }
        public SpeedUnits SpeedUnits { get; }

        public Speed(decimal amount, SpeedUnits speedUnits)
        {
            Amount = amount;
            SpeedUnits = speedUnits;
        }

        public override string ToString()
        {
            var unitsString = SpeedUnits switch
            {
                SpeedUnits.CentimetersPerSecond => "centimeters per second",
                _ => "meters per second"
            };

            return $"{Amount} {unitsString}";
        }
    }
}
