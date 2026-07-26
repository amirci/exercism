public enum Direction
{
    North = 0,
    East = 1,
    South = 2,
    West = 3
}

public class RobotSimulator
{
    private static readonly (int X, int Y)[] Steps =
    [
        (0, 1),
        (1, 0),
        (0, -1),
        (-1, 0),
    ];

    public RobotSimulator(Direction direction, int x, int y)
    {
        this.Direction = direction;
        this.X = x;
        this.Y = y;
    }

    public Direction Direction { get; private set; }

    public int X { get; private set; }

    public int Y { get; private set; }

    public void Move(string instructions)
    {
        foreach (var instruction in instructions)
        {
            Execute(instruction);
        }
    }

    private void Execute(char instruction)
    {
        switch (instruction)
        {
            case 'L':
                Direction = Turn(Direction, -1);
                break;
            case 'R':
                Direction = Turn(Direction, 1);
                break;
            case 'A':
                Advance();
                break;
        }
    }

    private static Direction Turn(Direction direction, int steps) =>
        (Direction)(((int)direction + steps + 4) % 4);

    private void Advance()
    {
        var step = Steps[(int)Direction];

        X += step.X;
        Y += step.Y;
    }
}
