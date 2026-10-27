type InputField = (" " | "*")[][];
type AnnotatedField = (number | "*")[][];
type Position = readonly [number, number];
type PositionCount = readonly [Position, number];

const NEIGHBOUR_OFFSETS: readonly Position[] = [
  [-1, -1], [-1, 0], [-1, 1],
  [0, -1],          [0, 1],
  [1, -1],  [1, 0],  [1, 1],
];

function generateAllPositions(field: InputField): Position[] {
  return field.flatMap((row, r) =>
    Array.from(row, (_, c) => [r, c] as const)
  );
}

function generateNeighbours([r, c]: Position): Position[] {
  return NEIGHBOUR_OFFSETS.map(([dr, dc]): Position => [r + dr, c + dc]);
}

function isFlower(element: string | undefined): boolean {
    return element === "*"
}

function countNeighbouringFlowers(field: InputField, [r, c]: Position): number {
  const toOneOrZero = ([row, col]: Position): number => isFlower(field[row]?.[col]) ? 1 : 0;

  return generateNeighbours([r, c])
    .map(toOneOrZero)
    .reduce((sum, count) => sum + count, 0);
}

function parseRows(rows: string[]): InputField {
  return rows.map(row =>
    Array.from(row, cell => {
      if (cell !== " " && cell !== "*") {
        throw new Error("Expected a space or flower");
      }
      return cell;
    })
  );
}

const assocCount = (board: AnnotatedField, [[r, c], flowers]: PositionCount): AnnotatedField => {
    board[r][c] = flowers;
    return board;
};

function formatRow(row: AnnotatedField[number]): string {
  return row.map(cell => cell === 0 ? " " : String(cell)).join("");
}

function positionWithCount(pos: Position, count: number): PositionCount {
    return [pos, count];
}

export function annotate(rows: string[]): string[] {
  const field = parseRows(rows);

  const emptyAnnotatedField: AnnotatedField = field.map(row => row.map(cell => cell === "*" ? "*" : 0));

  const onlyEmpty = ([r, c]: Position): boolean => !isFlower(field[r][c]);

  return generateAllPositions(field)
    .filter(onlyEmpty)
    .map(position => positionWithCount(position, countNeighbouringFlowers(field, position)))
    .reduce(assocCount, emptyAnnotatedField)
    .map(formatRow);
}
