import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class RateLimiter<K> {
    private final int limit;
    private final Duration windowSize;
    private final TimeSource timeSource;
    private final Map<K, Deque<Instant>> requestsByClient = new HashMap<>();

    public RateLimiter(int limit, Duration windowSize, TimeSource timeSource) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.timeSource = timeSource;
    }

    public boolean allow(K clientId) {
        var now = timeSource.now();
        var requests = recentRequestsFor(clientId, now);

        return addRequestIfAllowed(requests, now);
    }

    private boolean addRequestIfAllowed(Deque<Instant> requests, Instant now) {
        if (requests.size() >= limit) {
            return false;
        }

        requests.addLast(now);
        return true;
    }

    private Deque<Instant> recentRequestsFor(K clientId, Instant now) {
        var cutoff = now.minus(windowSize);
        var requests = requestsByClient.computeIfAbsent(clientId, id -> new ArrayDeque<>());

        while (!requests.isEmpty() && !requests.peekFirst().isAfter(cutoff)) {
            requests.removeFirst();
        }

        return requests;
    }
}
