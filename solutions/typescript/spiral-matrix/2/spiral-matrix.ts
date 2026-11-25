type Position = readonly [row: number, column: number];
type Side = { start: Position; direction: Position; length: number };

function sides(offset: number, width: number): Side[] {
  const end = offset + width - 1;

  return [
    { start: [offset, offset], direction: [0, 1], length: width },
    { start: [offset + 1, end], direction: [1, 0], length: width - 1 },
    { start: [end, end - 1], direction: [0, -1], length: width - 1 },
    { start: [end - 1, offset], direction: [-1, 0], length: width - 2 },
  ];
}

function* squareCoordinates(offset: number, width: number): Generator<Position> {
  for (const { start, direction, length } of sides(offset, width)) {
    for (let step = 0; step < length; step++) {
      yield [start[0] + direction[0] * step, start[1] + direction[1] * step];
    }
  }
}

function* spiralPositions(size: number): Generator<Position> {
  for (let offset = 0; offset < Math.ceil(size / 2); offset++) {
    const width = size - offset * 2;

    if (width === 1) {
      yield [offset, offset];
    } else {
      yield* squareCoordinates(offset, width);
    }
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
