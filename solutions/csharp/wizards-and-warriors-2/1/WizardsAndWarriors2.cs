static class GameMaster
{
    public static string Describe(Character ch) => $"You're a level {ch.Level} {ch.Class} with {ch.HitPoints} hit points.";

    public static string Describe(Destination dest) => $"You've arrived at {dest.Name}, which has {dest.Inhabitants} inhabitants.";

    public static string Describe(TravelMethod tm) => tm switch
    {
        TravelMethod.Walking => "You're traveling to your destination by walking.",
        TravelMethod.Horseback => "You're traveling to your destination on horseback.",
        _ => throw new ArgumentOutOfRangeException(nameof(tm))
    };

    public static string Describe(Character ch, Destination dest, TravelMethod tm) => $"{Describe(ch)} {Describe(tm)} {Describe(dest)}";

    public static string Describe(Character ch, Destination dest) => Describe(ch, dest, TravelMethod.Walking);
}

class Character
{
    public string Class { get; set; } = string.Empty;
    public int Level { get; set; }
    public int HitPoints { get; set; }
}

class Destination
{
    public string Name { get; set; } = string.Empty;
    public int Inhabitants { get; set; }
}

enum TravelMethod
{
    Walking,
    Horseback
}
