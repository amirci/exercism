public static class VariableLengthQuantity
{
    private const uint DataMask = 127;
    private const uint ContinuationBit = 128;

    public static uint[] Encode(uint[] numbers) =>
        numbers.SelectMany(EncodeNumber).ToArray();

    public static uint[] Decode(uint[] bytes)
    {
        EnsureLastByteEndsSequence(bytes);

        var numbers = new List<uint>();
        uint number = 0;

        foreach (var currentByte in bytes)
        {
            number = (number << 7) | (currentByte & DataMask);

            if (!HasContinuationBit(currentByte))
            {
                numbers.Add(number);
                number = 0;
            }
        }

        return numbers.ToArray();
    }

    private static void EnsureLastByteEndsSequence(uint[] bytes)
    {
        if (bytes.Length > 0 && HasContinuationBit(bytes[^1]))
        {
            throw new InvalidOperationException();
        }
    }

    private static bool HasContinuationBit(uint value) =>
        (value & ContinuationBit) != 0;

    private static uint[] EncodeNumber(uint number)
    {
        var bytes = new List<uint>();

        do
        {
            var chunk = number & DataMask;
            number >>= 7;

            if (bytes.Count > 0)
            {
                chunk |= ContinuationBit;
            }

            bytes.Insert(0, chunk);
        } while (number > 0);

        return bytes.ToArray();
    }
}
