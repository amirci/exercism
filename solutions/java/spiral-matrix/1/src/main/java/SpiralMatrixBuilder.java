import java.util.stream.IntStream;
import java.util.stream.Stream;

class SpiralMatrixBuilder {
    int[][] buildMatrixOfSize(int size) {
        var matrix = new int[size][size];
        var positions = spiralPositions(size).toList();

        for (var index = 0; index < positions.size(); index++) {
            var position = positions.get(index);
            matrix[position.row()][position.column()] = index + 1;
        }

        return matrix;
    }

    private Stream<Position> spiralPositions(int size) {
        return IntStream.range(0, (size + 1) / 2)
            .boxed()
            .flatMap(offset -> {
                var width = size - offset * 2;
                return width == 1 ? Stream.of(new Position(offset, offset)) : squareCoordinates(offset, width);
            });
    }

    private Stream<Position> squareCoordinates(int offset, int width) {
        return sides(offset, width)
            .flatMap(side -> IntStream.range(0, side.length()).mapToObj(side::positionAt));
    }

    private Stream<Side> sides(int offset, int width) {
        var end = offset + width - 1;

        return Stream.of(
            new Side(new Position(offset, offset), new Position(0, 1), width),
            new Side(new Position(offset + 1, end), new Position(1, 0), width - 1),
            new Side(new Position(end, end - 1), new Position(0, -1), width - 1),
            new Side(new Position(end - 1, offset), new Position(-1, 0), width - 2)
        );
    }

    private record Position(int row, int column) {}

    private record Side(Position start, Position direction, int length) {
        Position positionAt(int step) {
            return new Position(
                start.row() + direction.row() * step,
                start.column() + direction.column() * step
            );
        }
    }
}
