using Strand = string;

public static class Hamming
{
    public static int Distance(Strand first, Strand second)
    {
        if (first.Length != second.Length)
        {
            throw new ArgumentException("Strands must be of equal length.");
        }

        return first.Zip(second).Count(pair => pair.First != pair.Second);
    }
}
