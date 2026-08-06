public class LogLine {
    private final String logLine;

    public LogLine(String logLine) {
        this.logLine = logLine;
    }

    public LogLevel getLogLevel() {
        return LogLevel.fromCode(logLine.substring(1, 4));
    }

    public String getOutputForShortLog() {
        return getLogLevel().value() + ":" + message();
    }

    private String message() {
        return logLine.substring(logLine.indexOf(":") + 2);
    }
}
