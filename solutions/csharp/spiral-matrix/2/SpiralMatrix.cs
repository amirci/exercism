using Direction = (int Row, int Column);
using Position = (int Row, int Column);
using Side = ((int Row, int Column) Start, (int Row, int Column) Direction, int Length);

public class SpiralMatrix
{
    public static int[,] GetMatrix(int size)
    {
        var matrix = new int[size, size];

        foreach (var (position, value) in NumberedPositions(size))
        {
            matrix[position.Row, position.Column] = value;
        }

        return matrix;
    }

    private static IEnumerable<(Position Position, int Value)> NumberedPositions(int size) =>
        SpiralPositions(size).Select((position, index) => (position, index + 1));

    private static IEnumerable<Position> SpiralPositions(int size) =>
        from offset in Enumerable.Range(0, (size + 1) / 2)
        let width = size - offset * 2
        from position in width == 1 ? [(offset, offset)] : SquareCoordinates(offset, width)
        select position;

    private static IEnumerable<Position> SquareCoordinates(int offset, int width) =>
        from side in Sides(offset, width)
        from step in Enumerable.Range(0, side.Length)
        select (
            side.Start.Row + side.Direction.Row * step,
            side.Start.Column + side.Direction.Column * step
        );

    private static IEnumerable<Side> Sides(int offset, int width)
    {
        var end = offset + width - 1;

        return
        [
            ((offset, offset), (0, 1), width),
            ((offset + 1, end), (1, 0), width - 1),
            ((end, end - 1), (0, -1), width - 1),
            ((end - 1, offset), (-1, 0), width - 2),
        ];
    }
}
