using Position = (int Row, int Column);

public class SpiralMatrix
{
    public static int[,] GetMatrix(int size)
    {
        var matrix = new int[size, size];

        Populate(0, 1, size, matrix);

        return matrix;
    }

    private static void Populate(int offset, int start, int width, int[,] matrix)
    {
        if (width == 0)
        {
            return;
        }

        if (width == 1)
        {
            matrix[offset, offset] = start;
            return;
        }

        var number = start;

        foreach (var coordinate in SquareCoordinates(offset, width))
        {
            matrix[coordinate.Row, coordinate.Column] = number;
            number++;
        }

        Populate(offset + 1, number, width - 2, matrix);
    }

    private static IEnumerable<Position> SquareCoordinates(int offset, int width)
    {
        var end = offset + width;

        for (var column = offset; column < end; column++)
        {
            yield return (offset, column);
        }

        for (var row = offset + 1; row < end; row++)
        {
            yield return (row, end - 1);
        }

        for (var column = end - 2; column >= offset; column--)
        {
            yield return (end - 1, column);
        }

        for (var row = end - 2; row > offset; row--)
        {
            yield return (row, offset);
        }
    }
}
