using System.Collections.ObjectModel;

public class Authenticator(Identity admin)
{
    private class EyeColor
    {
        public string Blue = "blue";
        public string Green = "green";
        public string Brown = "brown";
        public string Hazel = "hazel";
        public string Grey = "grey";
    }

    private readonly IDictionary<string, Identity> _developers
        = new ReadOnlyDictionary<string, Identity>(new Dictionary<string, Identity>
        {
            ["Bertrand"] = new Identity { Email = "bert@ex.ism", EyeColor = "blue" },
            ["Anders"] = new Identity { Email = "anders@ex.ism", EyeColor = "brown" }
        });

    public Identity Admin { get; set; } = admin;

    public IDictionary<string, Identity> GetDevelopers() => _developers;
}

public struct Identity
{
    public string Email { get; set; }

    public string EyeColor { get; init; }
}
