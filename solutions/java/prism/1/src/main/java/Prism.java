import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Prism {
    // Refraction angles in the input are rounded to three decimal places.
    private static final double ANGLE_TOLERANCE = 0.01;

    public record LaserInfo(double x, double y, double angle) {
    }

    public record PrismInfo(int id, double x, double y, double angle) {
    }

    public static List<Integer> findSequence(LaserInfo laser, List<PrismInfo> prisms) {
        var sequence = new ArrayList<Integer>();
        var current = laser;

        while (true) {
            var next = nextPrism(current, prisms);
            if (next == null) {
                return sequence;
            }

            sequence.add(next.id());
            current = new LaserInfo(next.x(), next.y(),
                    normalizeAngle(current.angle() + next.angle()));
        }
    }

    private static PrismInfo nextPrism(LaserInfo laser, List<PrismInfo> prisms) {
        return prisms.stream()
                .filter(prism -> isOnBeam(laser, prism))
                .min(Comparator.comparingDouble(prism ->
                        Math.hypot(prism.x() - laser.x(), prism.y() - laser.y())))
                .orElse(null);
    }

    private static boolean isOnBeam(LaserInfo laser, PrismInfo prism) {
        double dx = prism.x() - laser.x();
        double dy = prism.y() - laser.y();
        if (dx == 0 && dy == 0) {
            return false;
        }

        double bearing = Math.toDegrees(Math.atan2(dy, dx));
        double difference = normalizeAngle(bearing - normalizeAngle(laser.angle()));
        return Math.min(difference, 360 - difference) <= ANGLE_TOLERANCE;
    }

    private static double normalizeAngle(double angle) {
        return (angle % 360 + 360) % 360;
    }
}
