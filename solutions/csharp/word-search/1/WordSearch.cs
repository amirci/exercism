using Direction = (int DeltaX, int DeltaY);
using Match = ((int, int), (int, int));
using Point = (int X, int Y);
using SearchResult = ((int, int), (int, int))?;

public class WordSearch
{
    private static readonly Direction[] Directions =
    [
        (1, 0),
        (-1, 0),
        (0, 1),
        (0, -1),
        (1, 1),
        (-1, -1),
        (1, -1),
        (-1, 1),
    ];

    private readonly string[] _rows;

    public WordSearch(string grid)
    {
        _rows = grid.Split('\n', StringSplitOptions.RemoveEmptyEntries);
    }

    public Dictionary<string, SearchResult> Search(string[] wordsToSearchFor) =>
        wordsToSearchFor.ToDictionary(word => word, FirstMatchFor);

    private SearchResult FirstMatchFor(string word) =>
        MatchesFor(word)
            .Select(match => (SearchResult)match)
            .FirstOrDefault();

    private IEnumerable<Match> MatchesFor(string word) =>
        from y in Enumerable.Range(0, _rows.Length)
        from x in Enumerable.Range(0, _rows[y].Length)
        from direction in Directions
        where Matches(word, (x, y), direction)
        let end = (
            x + direction.DeltaX * (word.Length - 1),
            y + direction.DeltaY * (word.Length - 1)
        )
        select (ToResultPoint((x, y)), ToResultPoint(end));

    private bool Matches(string word, Point start, Direction direction) =>
        Enumerable.Range(0, word.Length).All(offset =>
        {
            Point point = (
                start.X + direction.DeltaX * offset,
                start.Y + direction.DeltaY * offset
            );

            return InBounds(point) && _rows[point.Y][point.X] == word[offset];
        });

    private bool InBounds(Point point) =>
        point.Y >= 0
            && point.Y < _rows.Length
            && point.X >= 0
            && point.X < _rows[point.Y].Length;

    private static (int, int) ToResultPoint(Point point) => (point.X + 1, point.Y + 1);
}
