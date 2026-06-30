public static class TelemetryBuffer
{
    public static byte[] ToBuffer(long reading)
    {
        var buffer = new byte[9];

        var (prefix, byteCount) = reading switch
        {
            >= 4_294_967_296 => ((byte)0xf8, 8),
            >= 2_147_483_648 => ((byte)0x04, 4),
            >= 65_536 => ((byte)0xfc, 4),
            >= 0 => ((byte)0x02, 2),
            >= -32_768 => ((byte)0xfe, 2),
            >= -2_147_483_648 => ((byte)0xfc, 4),
            _ => ((byte)0xf8, 8),
        };

        buffer[0] = prefix;

        for (var i = 1; i <= byteCount; i++, reading >>= 8)
        {
            buffer[i] = (byte)(reading & 0xff);
        }

        return buffer;
    }

    public static long FromBuffer(byte[] buffer)
    {
        var (byteCount, signed) = buffer[0] switch
        {
            0x02 => (2, false),
            0xfe => (2, true),
            0x04 => (4, false),
            0xfc => (4, true),
            0xf8 => (8, true),
            _ => (0, false),
        };

        if (byteCount == 0)
        {
            return 0;
        }

        long value = 0;

        for (var i = 1; i <= byteCount; i++)
        {
            value |= (long)buffer[i] << ((i - 1) * 8);
        }

        return byteCount switch
        {
            2 when signed => (short)value,
            4 when signed => (int)value,
            _ => value,
        };
    }
}
