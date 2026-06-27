public class WeatherStation
{
    private Reading _lastReading;
    private int _readingCount;

    public void AcceptReading(Reading input)
    {
        _lastReading = input;
        _readingCount++;
    }

    public void ClearAll()
    {
        _lastReading = default;
        _readingCount = 0;
    }

    public decimal LatestTemperature => _lastReading.Temperature;

    public decimal LatestPressure => _lastReading.Pressure;

    public decimal LatestRainfall => _lastReading.Rainfall;

    public bool HasHistory => _readingCount > 1;

    private bool HasReadings => _readingCount > 0;
    
    public Outlook ShortTermOutlook
    {
        get
        {
            if (!HasReadings)
            {
                throw new ArgumentException();
            }

            if (_lastReading is { Pressure: < 10m, Temperature: < 30m })
            {
                return Outlook.Cool;
            }

            return _lastReading.Temperature > 50 ? Outlook.Good : Outlook.Warm;
        }
    }

    public Outlook LongTermOutlook
    {
        get
        {
            return _lastReading.WindDirection switch
            {
                WindDirection.Southerly => Outlook.Good,
                WindDirection.Easterly => _lastReading.Temperature > 20 ? Outlook.Good : Outlook.Warm,
                WindDirection.Northerly => Outlook.Cool,
                WindDirection.Westerly => Outlook.Rainy,
                _ => throw new ArgumentException(),
            };
        }
    }

    public State RunSelfTest()
    {
        return HasReadings ? State.Good : State.Bad;
    }
}

/*** Please do not modify this struct ***/
public record struct Reading
{
    public decimal Temperature { get; }
    public decimal Pressure { get; }
    public decimal Rainfall { get; }
    public WindDirection WindDirection { get; }

    public Reading(decimal temperature, decimal pressure,
        decimal rainfall, WindDirection windDirection)
    {
        Temperature = temperature;
        Pressure = pressure;
        Rainfall = rainfall;
        WindDirection = windDirection;
    }
}

/*** Please do not modify this enum ***/
public enum State
{
    Good,
    Bad
}

/*** Please do not modify this enum ***/
public enum Outlook
{
    Cool,
    Rainy,
    Warm,
    Good
}

/*** Please do not modify this enum ***/
public enum WindDirection
{
    Unknown, // default
    Northerly,
    Easterly,
    Southerly,
    Westerly
}
