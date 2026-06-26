
enum LogLevel
{
    Debug = 2,
    Error = 6,
    Fatal = 42,
    Info = 4,
    Trace = 1,
    Unknown = 0,
    Warning = 5
}

static class LogLine
{
    private static readonly Dictionary<string, LogLevel> StringToLevel = new()
    {
        ["DBG"] = LogLevel.Debug,
        ["ERR"] = LogLevel.Error,
        ["FTL"] = LogLevel.Fatal,
        ["INF"] = LogLevel.Info,
        ["TRC"] = LogLevel.Trace,
        ["WRN"] = LogLevel.Warning
    };
    
    public static LogLevel ParseLogLevel(string logLine)
    {
        var log = logLine[1..4];
        return StringToLevel.GetValueOrDefault(log, LogLevel.Unknown);
    }

    public static string OutputForShortLog(LogLevel logLevel, string message)
    {
        return $"{(int)logLevel}:{message}";
    }
}
