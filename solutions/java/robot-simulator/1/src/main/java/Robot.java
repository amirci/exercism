class Robot {
    private static final Step[] STEPS = {
        new Step(0, 1),
        new Step(1, 0),
        new Step(0, -1),
        new Step(-1, 0)
    };

    private GridPosition position;
    private Orientation orientation;

    Robot(GridPosition initialPosition, Orientation initialOrientation) {
        position = initialPosition;
        orientation = initialOrientation;
    }

    GridPosition getGridPosition() {
        return position;
    }

    Orientation getOrientation() {
        return orientation;
    }

    void advance() {
        var step = STEPS[orientation.ordinal()];
        position = new GridPosition(position.x + step.x(), position.y + step.y());
    }

    void turnLeft() {
        turn(-1);
    }

    void turnRight() {
        turn(1);
    }

    void simulate(String instructions) {
        for (var instruction : instructions.toCharArray()) {
            switch (instruction) {
                case 'L' -> turnLeft();
                case 'R' -> turnRight();
                case 'A' -> advance();
                default -> {}
            }
        }
    }

    private static Orientation[] ORIENTATIONS = Orientation.values();
    private static int ORIENTATIONS_LENGTH = ORIENTATIONS.length;

    private void turn(int steps) {
        var index = (orientation.ordinal() + steps + ORIENTATIONS_LENGTH) % ORIENTATIONS_LENGTH;

        orientation = ORIENTATIONS[index];
    }

    private record Step(int x, int y) {}
}
