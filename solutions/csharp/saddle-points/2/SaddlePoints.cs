using Index = (int Row, int Column);
using Bounds = (int[] RowMaxes, int[] ColumnMins);

public static class SaddlePoints
{
    public static IEnumerable<(int, int)> Calculate(int[,] matrix)
    {
        var (rowMaxes, columnMins) = BoundsFor(matrix);

        return Indices(matrix)
            .Where(IsSaddlePoint)
            .Select(index => (index.Row + 1, index.Column + 1));

        bool IsSaddlePoint(Index index)
        {
            var (row, column) = index;
            var value = matrix[row, column];

            return (rowMaxes[row], columnMins[column]) == (value, value);
        }
    }

    private static Bounds BoundsFor(int[,] matrix)
    {
        int rows = matrix.GetLength(0);
        int cols = matrix.GetLength(1);

        int[] rowMaxes = Enumerable.Repeat(int.MinValue, rows).ToArray();
        int[] columnMins = Enumerable.Repeat(int.MaxValue, cols).ToArray();

        foreach (var (row, column) in Indices(matrix))
        {
            rowMaxes[row] = Math.Max(rowMaxes[row], matrix[row, column]);
            columnMins[column] = Math.Min(columnMins[column], matrix[row, column]);
        }

        return (rowMaxes, columnMins);
    }

    private static IEnumerable<Index> Indices(int[,] matrix)
    {
        for (var row = 0; row < matrix.GetLength(0); row++)
        {
            for (var column = 0; column < matrix.GetLength(1); column++)
            {
                yield return (row, column);
            }
        }
    }
}
