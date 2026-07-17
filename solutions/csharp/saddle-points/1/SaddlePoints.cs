using Index = (int Row, int Column);
using Points = (int[] RowMaxes, int[] ColumnMins);

public static class SaddlePoints
{
    public static IEnumerable<(int, int)> Calculate(int[,] matrix)
    {
        var (rowMaxes, columnMins) = InitializePoints(matrix);

        foreach (var (row, col) in Indices(matrix))
        {
            rowMaxes[row] = Math.Max(rowMaxes[row], matrix[row, col]);
            columnMins[col] = Math.Min(columnMins[col], matrix[row, col]);
        }

        return Indices(matrix)
            .Where(IsSaddlePoint)
            .Select(index => (index.Row + 1, index.Column + 1));

        bool IsSaddlePoint(Index index)
        {
            var (row, column) = index;
            var value = matrix[row, column];

            return rowMaxes[row] == value && columnMins[column] == value;
        }
    }

    private static Points InitializePoints(int[,] matrix)
    {
        int rows = matrix.GetLength(0);
        int cols = matrix.GetLength(1);

        int[] rowMaxes = Enumerable.Repeat(int.MinValue, rows).ToArray();
        int[] columnMins = Enumerable.Repeat(int.MaxValue, cols).ToArray();

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
