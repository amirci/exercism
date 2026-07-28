using FilledAmount = int;
using State = (int AmountStart, int AmountOther);
using WaterCapacity = int;

public enum Bucket
{
    One,
    Two
}

public readonly record struct TwoBucketResult(int Moves, Bucket GoalBucket, int OtherBucket);

public record TwoBucket(int CapacityOne, int CapacityTwo, Bucket Start)
{
    private readonly BucketInfo _start = BucketInfoFor(Start, CapacityOne, CapacityTwo);

    private readonly BucketInfo _other = BucketInfoFor(Other(Start), CapacityOne, CapacityTwo);

    public TwoBucketResult Measure(int goal)
    {
        EnsurePossible(goal);

        var start = InitialState();
        var queue = new Queue<(State State, int Moves)>();
        var seen = new HashSet<State>();

        queue.Enqueue((start, 1));
        seen.Add(start);

        while (queue.Count > 0)
        {
            var (state, moves) = queue.Dequeue();

            if (HasGoal(state, goal))
            {
                return ResultFor(state, moves, goal);
            }

            foreach (var next in NextStates(state).Where(IsAllowed))
            {
                if (seen.Add(next))
                {
                    queue.Enqueue((next, moves + 1));
                }
            }
        }

        throw new ArgumentException();
    }

    private State InitialState() => (_start.Capacity, 0);

    private TwoBucketResult ResultFor(State state, int moves, int goal) =>
        state.AmountStart == goal ? new(moves, _start.Id, state.AmountOther) : new(moves, _other.Id, state.AmountStart);

    private static bool HasGoal(State state, int goal) => state.AmountStart == goal || state.AmountOther == goal;

    private IEnumerable<State> NextStates(State state)
    {
        yield return (_start.Capacity, state.AmountOther);
        yield return (state.AmountStart, _other.Capacity);
        yield return (0, state.AmountOther);
        yield return (state.AmountStart, 0);
        yield return Pour(state.AmountStart, state.AmountOther, _other.Capacity);

        var (other, start) = Pour(state.AmountOther, state.AmountStart, _start.Capacity);
        yield return (start, other);
    }

    private static State Pour(FilledAmount source, FilledAmount target, WaterCapacity targetCapacity)
    {
        var amount = Math.Min(source, targetCapacity - target);
        return (source - amount, target + amount);
    }

    private bool IsAllowed(State state) => !_start.IsEmpty(state.AmountStart) || !_other.IsFull(state.AmountOther);

    private void EnsurePossible(int goal)
    {
        if (goal > Math.Max(CapacityOne, CapacityTwo) || goal % Gcd(CapacityOne, CapacityTwo) != 0)
        {
            throw new ArgumentException();
        }
    }

    private static int Gcd(int a, int b)
    {
        while (b != 0)
        {
            (a, b) = (b, a % b);
        }

        return a;
    }

    private static BucketInfo BucketInfoFor(Bucket bucket, WaterCapacity capacityOne, WaterCapacity capacityTwo) =>
        bucket == Bucket.One
            ? new(Bucket.One, capacityOne)
            : new(Bucket.Two, capacityTwo);

    private static Bucket Other(Bucket bucket) =>
        bucket == Bucket.One ? Bucket.Two : Bucket.One;

    private readonly record struct BucketInfo(Bucket Id, WaterCapacity Capacity)
    {
        public bool IsFull(FilledAmount amount) => amount == Capacity;

        public bool IsEmpty(FilledAmount amount) => amount == 0;
    }
}
