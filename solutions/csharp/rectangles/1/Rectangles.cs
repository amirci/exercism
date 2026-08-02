public static class Rectangles
{
    public static int Count(string[] rows)
    {
        if (rows.Length == 0 || rows[0].Length == 0)
            return 0;

        return RectanglesIn(rows).Count();
    }

    private static IEnumerable<(int Top, int Bottom, int Left, int Right)> RectanglesIn(string[] rows) =>
        from top in Enumerable.Range(0, rows.Length - 1)
        from left in Enumerable.Range(0, rows[top].Length - 1)
        from right in Enumerable.Range(left + 1, rows[top].Length - left - 1)
        where IsHorizontalEdge(rows[top], left, right)
        from bottom in Enumerable.Range(top + 1, rows.Length - top - 1)
        where IsRectangle(rows, top, bottom, left, right)
        select (top, bottom, left, right);

    private static bool IsRectangle(string[] rows, int top, int bottom, int left, int right) =>
        IsHorizontalEdge(rows[bottom], left, right)
            && IsVerticalEdge(rows, top, bottom, left)
            && IsVerticalEdge(rows, top, bottom, right);

    private static bool IsHorizontalEdge(string row, int left, int right) =>
        row[left] == '+'
            && row[right] == '+'
            && row[left..(right + 1)].All(character => character is '+' or '-');

    private static bool IsVerticalEdge(string[] rows, int top, int bottom, int column) =>
        Enumerable.Range(top, bottom - top + 1)
            .All(row => rows[row][column] is '+' or '|');
}
