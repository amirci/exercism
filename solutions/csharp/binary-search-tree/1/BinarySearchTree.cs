using System.Collections;

public class BinarySearchTree : IEnumerable<int>
{
    public BinarySearchTree(int value) => Value = value;

    public BinarySearchTree(IEnumerable<int> values)
    {
        using var enumerator = values.GetEnumerator();
        enumerator.MoveNext();

        Value = enumerator.Current;

        while (enumerator.MoveNext())
            Add(enumerator.Current);
    }

    public int Value { get; }

    public BinarySearchTree? Left { get; private set; }

    public BinarySearchTree? Right { get; private set; }

    public BinarySearchTree Add(int value)
    {
        if (value <= Value)
            Left = Left?.Add(value) ?? new BinarySearchTree(value);
        else
            Right = Right?.Add(value) ?? new BinarySearchTree(value);

        return this;
    }

    public IEnumerator<int> GetEnumerator()
    {
        if (Left is not null)
        {
            foreach (var value in Left)
                yield return value;
        }

        yield return Value;

        if (Right is not null)
        {
            foreach (var value in Right)
                yield return value;
        }
    }

    IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
}
