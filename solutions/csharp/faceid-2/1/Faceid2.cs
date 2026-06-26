public record FacialFeatures(string EyeColor, decimal PhiltrumWidth);

public record Identity(string Email, FacialFeatures FacialFeatures);

public class Authenticator
{
    public static bool AreSameFace(FacialFeatures faceA, FacialFeatures faceB)
    {
        return faceA == faceB;
    }

    private static readonly Identity Admin = new Identity("admin@exerc.ism", new FacialFeatures("green", 0.9m));
        
    public bool IsAdmin(Identity identity) => identity == Admin;

    private readonly ISet<Identity> _registered = new HashSet<Identity>();
    
    public bool Register(Identity identity)
    {
        return _registered.Add(identity);
    }

    public bool IsRegistered(Identity identity)
    {
        return _registered.Contains(identity);
    }

    public static bool AreSameObject(Identity identityA, Identity identityB)
    {
        return ReferenceEquals(identityA, identityB);
    }
}
