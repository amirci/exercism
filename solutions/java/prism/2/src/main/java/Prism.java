import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Prism {
    // Refraction angles in the input are rounded to three decimal places.
    private static final double ANGLE_TOLERANCE = 0.01;

    public record LaserInfo(double x, double y, double angle) {
    }

    public record PrismInfo(int id, double x, double y, double angle) {
    }

    public static List<Integer> findSequence(LaserInfo laser, List<PrismInfo> prisms) {
        record Step(int id, LaserInfo laser) {
        }

        Function<LaserInfo, Step> advance = current -> {
            var prism = nextPrism(current, prisms);
            return prism == null ? null : new Step(prism.id(), deflect(current, prism));
        };

        return Stream.iterate(
                        advance.apply(laser),
                        Objects::nonNull,
                        step -> advance.apply(step.laser()))
                .map(Step::id)
                .toList();
    }

    private static LaserInfo deflect(LaserInfo laser, PrismInfo prism) {
        return new LaserInfo(prism.x(), prism.y(), normalizeAngle(laser.angle() + prism.angle()));
    }

    private static PrismInfo nextPrism(LaserInfo laser, List<PrismInfo> prisms) {
        return prisms.stream()
                .filter(isOnBeam(laser))
                .min(distanceTo(laser))
                .orElse(null);
    }

    private static Comparator<PrismInfo> distanceTo(LaserInfo laser) {
        return Comparator.comparingDouble(prism -> Math.hypot(prism.x() - laser.x(), prism.y() - laser.y()));
    }

    private static Predicate<PrismInfo> isOnBeam(LaserInfo laser) {
        return prism -> {
            var dx = prism.x() - laser.x();
            var dy = prism.y() - laser.y();
            if (dx == 0 && dy == 0) {
                return false;
            }

            var bearing = Math.toDegrees(Math.atan2(dy, dx));
            var difference = normalizeAngle(bearing - normalizeAngle(laser.angle()));
            return Math.min(difference, 360 - difference) <= ANGLE_TOLERANCE;
        };
    }

    private static double normalizeAngle(double angle) {
        return (angle % 360 + 360) % 360;
    }
}
