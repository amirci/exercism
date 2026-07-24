public record Tree(char Value, Tree? Left, Tree? Right);

public static class Satellite
{
    public static Tree? TreeFromTraversals(char[] preOrder, char[] inOrder)
    {
        if (!ValidTraversals(preOrder, inOrder))
        {
            throw new ArgumentException();
        }

        return Build(preOrder, inOrder);
    }

    private static Tree? Build(ReadOnlySpan<char> preOrder, ReadOnlySpan<char> inOrder)
    {
        if (preOrder.IsEmpty)
        {
            return null;
        }

        var root = preOrder[0];
        var rootIndex = inOrder.IndexOf(root);
        var leftSize = rootIndex;

        return new Tree(
            root,
            Build(preOrder.Slice(1, leftSize), inOrder[..rootIndex]),
            Build(preOrder[(leftSize + 1)..], inOrder[(rootIndex + 1)..]));
    }

    private static bool ValidTraversals(char[] preOrder, char[] inOrder)
    {
        var preOrderItems = preOrder.ToHashSet();
        var inOrderItems = inOrder.ToHashSet();

        return preOrder.Length == inOrder.Length
            && preOrderItems.Count == preOrder.Length
            && inOrderItems.Count == inOrder.Length
            && preOrderItems.SetEquals(inOrderItems);
    }
}
