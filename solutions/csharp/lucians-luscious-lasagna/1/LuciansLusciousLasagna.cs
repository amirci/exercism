class Lasagna
{
    internal int ExpectedMinutesInOven() => 40;

    internal int RemainingMinutesInOven(int elapsedMinutes) => ExpectedMinutesInOven() - elapsedMinutes;

    internal int PreparationTimeInMinutes(int layers) => layers * 2;

    internal int ElapsedTimeInMinutes(int layers, int elapsedMinutes) => elapsedMinutes + PreparationTimeInMinutes(layers);
}
