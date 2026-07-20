public readonly record struct Coord(ushort X, ushort Y);

public readonly record struct Plot(Coord C1, Coord C2, Coord C3, Coord C4)
{
    public int LongestSide
    {
        get
        {
            ushort[] xs = [C1.X, C2.X, C3.X, C4.X];
            ushort[] ys = [C1.Y, C2.Y, C3.Y, C4.Y];

            return Math.Max(xs.Max() - xs.Min(), ys.Max() - ys.Min());
        }
    }
}

public class ClaimsHandler
{
    private readonly HashSet<Plot> _claims = [];
    private Plot _lastClaim;
    private Plot _longestSide;

    public void StakeClaim(Plot plot)
    {
        _claims.Add(plot);
        _lastClaim = plot;
        _longestSide = _claims.Count == 1 || plot.LongestSide > _longestSide.LongestSide
            ? plot
            : _longestSide;
    }

    public bool IsClaimStaked(Plot plot) => _claims.Contains(plot);

    public bool IsLastClaim(Plot plot) => _lastClaim == plot;

    public Plot GetClaimWithLongestSide() => _longestSide;
}
