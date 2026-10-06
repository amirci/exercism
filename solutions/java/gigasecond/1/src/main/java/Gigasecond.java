import java.time.LocalDate;
import java.time.LocalDateTime;

public class Gigasecond {
    private static final long GIGASECOND = 1_000_000_000L;
    private final LocalDateTime moment;

    public Gigasecond(LocalDate moment) {
        this(moment.atStartOfDay());
    }

    public Gigasecond(LocalDateTime moment) {
        this.moment = moment;
    }

    public LocalDateTime getDateTime() {
        return moment.plusSeconds(GIGASECOND);
    }
}
