class WeighingMachine
{
    private double _weight;

    public WeighingMachine(int precision)
    {
        Precision = precision;
    }

    public int Precision { get; }

    public double Weight
    {
        get => _weight;
        set
        {
            if (value < 0)
            {
                throw new ArgumentOutOfRangeException();
            }

            _weight = value;
        }
    }

    public double TareAdjustment { get; set; } = 5.0;

    public string DisplayWeight => Format(Weight - TareAdjustment) + " kg";

    private string Format(double value) => value.ToString($"F{Precision}");
}
