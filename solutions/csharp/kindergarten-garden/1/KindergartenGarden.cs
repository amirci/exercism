public enum Plant
{
    Violets,
    Radishes,
    Clover,
    Grass
}

public class KindergartenGarden(string diagram)
{
    private readonly string[] rows = diagram.Split("\n");

    private enum Student
    {
        Alice, Bob, Charlie, David, Eve, Fred,
        Ginny, Harriet, Ileana, Joseph, Kincaid, Larry
    }

    public IEnumerable<Plant> Plants(string student)
    {
        var offset = 2 * (int)Enum.Parse<Student>(student);
        return rows
            .SelectMany(row => row.Skip(offset).Take(2))
            .Select(ToPlant);
    }

    private static Plant ToPlant(char code) => code switch
    {
        'V' => Plant.Violets,
        'R' => Plant.Radishes,
        'C' => Plant.Clover,
        'G' => Plant.Grass,
        _ => throw new ArgumentException("Unknown plant code.", nameof(code))
    };
}
