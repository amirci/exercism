static class AssemblyLine
{
    public static double SuccessRate(int speed) => speed switch
    {
        < 1 => 0.0,
        < 5 => 1.0,
        < 9 => 0.9,
        < 10 => 0.8,
        _ => 0.77
    };

    private const double CarsProducedPerHour = 221.0;

    public static double ProductionRatePerHour(int speed) =>
        speed * CarsProducedPerHour * SuccessRate(speed);

    public static int WorkingItemsPerMinute(int speed) => (int)(ProductionRatePerHour(speed) / 60);
}
