class RemoteControlCar
{
    private IPowerState _state = new Charged();

    public static RemoteControlCar Buy() => new();

    public string DistanceDisplay() => $"Driven {_state.Meters} meters";

    public string BatteryDisplay() => _state.BatteryDisplay();

    public void Drive()
    {
        _state = _state.Advance();
    }

    private interface IPowerState
    {
        int Meters { get; }
        IPowerState Advance();
        string BatteryDisplay();
    }

    private class Empty(int drivenMeters) : IPowerState
    {
        public int Meters => drivenMeters;
        public IPowerState Advance() => this;
        public string BatteryDisplay() => "Battery empty";
    }

    private class Charged : IPowerState
    {
        private int _charge = 100;

        public int Meters { get; private set; }

        public IPowerState Advance()
        {
            Meters += 20;
            _charge--;

            return _charge == 0 ? new Empty(Meters) : this;
        }

        public string BatteryDisplay() => $"Battery at {_charge}%";
    }
}
