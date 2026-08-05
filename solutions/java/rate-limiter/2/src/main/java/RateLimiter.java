import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RateLimiter<K> {
    private final int limit;
    private final Duration windowSize;
    private final TimeSource timeSource;
    private final Map<K, RequestsWindow> requestsByClient = new HashMap<>();

    public RateLimiter(int limit, Duration windowSize, TimeSource timeSource) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.timeSource = timeSource;
    }

    public boolean allow(K clientId) {
        var now = timeSource.now();
        var window = findWindowOrCreateOne(clientId, now);

        if (window.hasExpired(now, windowSize)) {
            window = resetWindow(clientId, now);
        }

        return window.addRequestIfAllowed(limit);
    }

    private RequestsWindow findWindowOrCreateOne(K clientId, Instant now) {
        return requestsByClient.computeIfAbsent(clientId, ignored -> new RequestsWindow(now));
    }

    private RequestsWindow resetWindow(K clientId, Instant now) {
        var window = new RequestsWindow(now);
        requestsByClient.put(clientId, window);
        return window;
    }

    private static class RequestsWindow {
        private final Instant start;
        private int count;

        RequestsWindow(Instant start) {
            this.start = start;
        }

        boolean hasExpired(Instant now, Duration windowSize) {
            return !now.minus(windowSize).isBefore(start);
        }

        boolean addRequestIfAllowed(int limit) {
            if (count >= limit) {
                return false;
            }

            count++;
            return true;
        }
    }
}
