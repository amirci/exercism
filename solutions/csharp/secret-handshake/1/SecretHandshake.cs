using CommandByBit = (int Bit, string Command);

public static class SecretHandshake
{
    private const int ReverseBit = 16;

    private static readonly CommandByBit[] CommandsByBit =
    [
        (1, "wink"),
        (2, "double blink"),
        (4, "close your eyes"),
        (8, "jump")
    ];

    public static string[] Commands(int commandValue)
    {
        bool IsBitOn(CommandByBit command) => Includes(commandValue, command.Bit);

        var commands = CommandsByBit
            .Where(IsBitOn)
            .Select(command => command.Command)
            .ToArray();

        if (Includes(commandValue, ReverseBit))
        {
            Array.Reverse(commands);
        }

        return commands;
    }

    private static bool Includes(int commandValue, int bit) =>
        (commandValue & bit) != 0;
}
