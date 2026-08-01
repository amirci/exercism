using Sprache;

public record SgfTree
{
    public SgfTree(IDictionary<string, string[]> data, params SgfTree[] children)
    {
        Data = data;
        Children = children;
    }

    public IDictionary<string, string[]> Data { get; }
    public SgfTree[] Children { get; }
}

public class SgfParser
{
    public static SgfTree ParseTree(string input)
    {
        try
        {
            return Tree.End().Parse(input);
        }
        catch (ParseException exception)
        {
            throw new ArgumentException("Invalid SGF tree.", exception);
        }
    }

    private static readonly Parser<SgfTree> Tree =
        from _ in Parse.Char('(')
        from nodes in Node.AtLeastOnce()
        from children in Parse.Ref(() => Tree).Many()
        from __ in Parse.Char(')')
        select BuildTree(nodes.ToArray(), children.ToArray());

    private static readonly Parser<SgfTree> Node =
        from _ in Parse.Char(';')
        from properties in Property.Many()
        select new SgfTree(properties.ToDictionary(property => property.Key, property => property.Values));

    private static readonly Parser<(string Key, string[] Values)> Property =
        from key in Parse.Upper.AtLeastOnce().Text()
        from values in Value.AtLeastOnce()
        select (key, values.ToArray());

    private static readonly Parser<string> EscapedCharacter =
        from _ in Parse.Char('\\')
        from character in Parse.AnyChar
        select NormalizeEscaped(character);

    private static readonly Parser<string> UnescapedCharacter = Parse.CharExcept("\\]").Select(Normalize);

    private static readonly Parser<string> ValueText = EscapedCharacter.Or(UnescapedCharacter);

    private static readonly Parser<string> Value =
        ValueText.Many()
            .Select(string.Concat)
            .Contained(Parse.Char('['), Parse.Char(']'));

    private static SgfTree BuildTree(SgfTree[] nodes, SgfTree[] children)
    {
        var tree = new SgfTree(nodes[^1].Data, children);

        for (var index = nodes.Length - 2; index >= 0; index--)
            tree = new SgfTree(nodes[index].Data, tree);

        return tree;
    }

    private static string NormalizeEscaped(char character) => character == '\n' ? "" : Normalize(character);

    private static string Normalize(char character) => char.IsWhiteSpace(character) && character != '\n' ? " " : character.ToString();
}
