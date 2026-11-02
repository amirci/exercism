public static class TelemetryBuffer
{
    public static byte[] ToBuffer(long reading)
    {
        var (prefix, bytes) = reading switch
        {
            > uint.MaxValue or < int.MinValue => ((byte)0xf8, BitConverter.GetBytes(reading)),
            > int.MaxValue => ((byte)0x04, BitConverter.GetBytes((uint)reading)),
            > ushort.MaxValue or < short.MinValue => ((byte)0xfc, BitConverter.GetBytes((int)reading)),
            >= 0 => ((byte)0x02, BitConverter.GetBytes((ushort)reading)),
            _ => ((byte)0xfe, BitConverter.GetBytes((short)reading)),
        };

        var buffer = new byte[9];
        buffer[0] = prefix;
        bytes.CopyTo(buffer, 1);
        return buffer;
    }

    public static long FromBuffer(byte[] buffer) =>
        (sbyte)buffer[0] switch
        {
            -8 => BitConverter.ToInt64(buffer, 1),
            4 => BitConverter.ToUInt32(buffer, 1),
            -4 => BitConverter.ToInt32(buffer, 1),
            2 => BitConverter.ToUInt16(buffer, 1),
            -2 => BitConverter.ToInt16(buffer, 1),
            _ => 0,
        };
}
