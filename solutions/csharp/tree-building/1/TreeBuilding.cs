public class TreeBuildingRecord
{
    public int ParentId { get; set; }
    public int RecordId { get; set; }
}

public class Tree
{
    public int Id { get; set; }
    public int ParentId { get; set; }

    public List<Tree> Children { get; } = [];

    public bool IsLeaf => Children.Count == 0;
}

public static class TreeBuilder
{
    public static Tree BuildTree(IEnumerable<TreeBuildingRecord> records)
    {
        var orderedRecords = records.OrderBy(record => record.RecordId).ToArray();

        Validate(orderedRecords);

        var trees = CreateTrees(orderedRecords);

        LinkChildren(trees);

        return trees[0];
    }

    private static void Validate(TreeBuildingRecord[] records)
    {
        if (records.Length == 0)
            throw new ArgumentException();

        if (records[0].ParentId != 0)
            throw new ArgumentException();

        if (records.Select((record, id) => (record, id)).Any(IsInvalidRecord))
            throw new ArgumentException();
    }

    private static bool IsInvalidRecord((TreeBuildingRecord Record, int Id) pair)
    {
        var (record, id) = pair;

        return record.RecordId != id || (id != 0 && record.ParentId >= id);
    }

    private static Tree[] CreateTrees(TreeBuildingRecord[] records) =>
        records.Select(record => new Tree { Id = record.RecordId, ParentId = record.ParentId }).ToArray();

    private static void LinkChildren(Tree[] trees)
    {
        for (var id = 1; id < trees.Length; id++)
        {
            var tree = trees[id];
            trees[tree.ParentId].Children.Add(tree);
        }
    }
}
