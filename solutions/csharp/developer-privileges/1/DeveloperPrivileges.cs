public class Authenticator
{
    public Authenticator()
    {
        Admin = CreateIdentity("admin@ex.ism", "green", 0.9M, "Chanakya", "Mumbai", "India");

        Developers = new Dictionary<string, Identity>
        {
            ["Bertrand"] = CreateIdentity("bert@ex.ism", "blue", 0.8M, "Bertrand", "Paris", "France"),
            ["Anders"] = CreateIdentity("anders@ex.ism", "brown", 0.85M, "Anders", "Redmond", "USA"),
        };
    }

    public Identity Admin { get; }

    public IDictionary<string, Identity> Developers { get; }

    private static Identity CreateIdentity(string email, string eyeColor, decimal philtrumWidth, params string[] nameAndAddress) =>
        new()
        {
            Email = email,
            FacialFeatures = new() { EyeColor = eyeColor, PhiltrumWidth = philtrumWidth },
            NameAndAddress = nameAndAddress
        };
}

//**** please do not modify the FacialFeatures class ****
public class FacialFeatures
{
    public required string EyeColor { get; set; }
    public required decimal PhiltrumWidth { get; set; }
}

//**** please do not modify the Identity class ****
public class Identity
{
    public required string Email { get; set; }
    public required FacialFeatures FacialFeatures { get; set; }
    public required IList<string> NameAndAddress { get; set; }
}
