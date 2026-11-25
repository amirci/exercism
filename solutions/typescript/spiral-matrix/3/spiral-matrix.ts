type Position = readonly [row: number, column: number];

function* spiralPositions(size: number): Generator<Position> {
  let row = 0;
  let column = -1;
  let direction = 1;

  function* walk(steps: number, rowDelta: number, columnDelta: number): Generator<Position> {
    for (let step = 0; step < steps; step++) {
      row += rowDelta;
      column += columnDelta;
      yield [row, column];
    }
  }

  for (let length = size; length > 0; length--) {
    yield* walk(length    , 0, direction);
    yield* walk(length - 1, direction, 0);

    direction *= -1;
  }
}

export function ofSize(size: number): number[][] {
  const matrix = Array.from({ length: size }, () => Array<number>(size).fill(0));
  let value = 1;

  for (const [row, column] of spiralPositions(size)) {
    matrix[row][column] = value++;
  }

  return matrix;
}
