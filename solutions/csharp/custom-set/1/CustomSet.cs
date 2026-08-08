public class CustomSet
{
    private readonly HashSet<int> values;

    public CustomSet(params int[] values) => this.values = values.ToHashSet();

    public CustomSet Add(int value)
    {
        values.Add(value);
        return this;
    }

    public bool Empty() => values.Count == 0;

    public bool Contains(int value) => values.Contains(value);

    public bool Subset(CustomSet right) => values.IsSubsetOf(right.values);

    public bool Disjoint(CustomSet right) => !values.Overlaps(right.values);

    public CustomSet Intersection(CustomSet right) => new([.. values.Intersect(right.values)]);

    public CustomSet Difference(CustomSet right) => new([.. values.Except(right.values)]);

    public CustomSet Union(CustomSet right) => new([.. values.Union(right.values)]);

    public override bool Equals(object? obj) => obj is CustomSet other && values.SetEquals(other.values);

    public override int GetHashCode() => values.Aggregate(0, HashCode.Combine);
}
