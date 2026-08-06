public enum LogLevel {
    UNKNOWN("", 0),
    TRACE("TRC", 1),
    DEBUG("DBG", 2),
    INFO("INF", 4),
    WARNING("WRN", 5),
    ERROR("ERR", 6),
    FATAL("FTL", 42);

    private final String code;
    private final int value;

    LogLevel(String code, int value) {
        this.code = code;
        this.value = value;
    }

    static LogLevel fromCode(String code) {
        for (var level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }

        return UNKNOWN;
    }

    int value() {
        return value;
    }
}
