public readonly record struct RailFenceCipher(int Rails)
{
    private int Period => 2 * (Rails - 1);

    public string Encode(string input) =>
        string.Concat(ZigZagIndexes(input.Length)
            .Select(index => input[index]));

    public string Decode(string input)
    {
        var decoded = new char[input.Length];

        foreach (var (targetIndex, encodedIndex) in DecodingIndexes(input.Length))
        {
            decoded[targetIndex] = input[encodedIndex];
        }

        return new string(decoded);
    }

    private IEnumerable<int> ZigZagIndexes(int length) =>
        Enumerable.Range(0, length).OrderBy(TargetRail);

    private IEnumerable<(int TargetIndex, int EncodedIndex)> DecodingIndexes(int length) =>
        ZigZagIndexes(length).Select((targetIndex, encodedIndex) => (targetIndex, encodedIndex));

    private int TargetRail(int index)
    {
        var position = index % Period;

        return position < Rails
            ? position
            : Period - position;
    }
}
