public class BowlingGame
{
    private readonly List<int> _rolls = [];
    private readonly List<FrameScore> _frames = [];
    private GameState _state = new FirstRoll(Frame: 1);

    public void Roll(int pins)
    {
        if (pins is < 0 or > 10)
            throw new ArgumentException();

        var (newState, score) = _state.Next(pins, _rolls.Count);

        _state = newState;
        _rolls.Add(pins);

        if (score is not null)
            _frames.Add(score);
    }

    public int? Score()
    {
        if (_state is not Complete)
            throw new ArgumentException();

        return _frames
            .Sum(frame => frame.Score(_rolls));
    }

    private static void EnsureTwoRollsDontExceedTen(int first, int second)
    {
        if (first + second > 10)
            throw new ArgumentException();
    }

    private abstract record GameState
    {
        public abstract (GameState State, FrameScore? Score) Next(int pins, int rollIndex);
    }

    private sealed record FirstRoll(int Frame) : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex) =>
            pins switch
            {
                10 when Frame == 10 => (new TenthStrikeBonusFirst(), new StrikeScore(rollIndex)),
                10 => (new FirstRoll(Frame + 1), new StrikeScore(rollIndex)),
                _ when Frame == 10 => (new TenthSecondRoll(pins, rollIndex), null),
                _ => (new SecondRoll(Frame, pins, rollIndex), null)
            };
    }

    private sealed record SecondRoll(int Frame, int First, int FrameIndex) : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex)
        {
            EnsureTwoRollsDontExceedTen(First, pins);

            FrameScore score = First + pins == 10
                ? new SpareScore(FrameIndex)
                : new OpenFrameScore(FrameIndex);

            return (new FirstRoll(Frame + 1), score);
        }
    }

    private sealed record TenthSecondRoll(int First, int FrameIndex) : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex)
        {
            EnsureTwoRollsDontExceedTen(First, pins);

            return First + pins == 10
                ? (new TenthSpareBonus(FrameIndex), null)
                : (new Complete(), new OpenFrameScore(FrameIndex));
        }
    }

    private sealed record TenthSpareBonus(int FrameIndex) : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex) =>
            (new Complete(), new SpareScore(FrameIndex));
    }

    private sealed record TenthStrikeBonusFirst : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex) =>
            (new TenthStrikeBonusSecond(pins), null);
    }

    private sealed record TenthStrikeBonusSecond(int FirstBonus) : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex)
        {
            if (FirstBonus != 10 && FirstBonus + pins > 10)
                throw new ArgumentException();

            return (new Complete(), null);
        }
    }

    private sealed record Complete : GameState
    {
        public override (GameState State, FrameScore? Score) Next(int pins, int rollIndex) =>
            throw new ArgumentException();
    }

    private abstract record FrameScore(int Index)
    {
        public abstract int Score(IReadOnlyList<int> rolls);
    }

    private abstract record ThreeRollScore(int Index) : FrameScore(Index)
    {
        public override int Score(IReadOnlyList<int> rolls) => rolls[Index] + rolls[Index + 1] + rolls[Index + 2];
    }

    private sealed record StrikeScore(int Index) : ThreeRollScore(Index);

    private sealed record SpareScore(int Index) : ThreeRollScore(Index);

    private sealed record OpenFrameScore(int Index) : FrameScore(Index)
    {
        public override int Score(IReadOnlyList<int> rolls) => rolls[Index] + rolls[Index + 1];
    }
}
