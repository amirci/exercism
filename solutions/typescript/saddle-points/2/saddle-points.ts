type Position = { row: number; column: number };

const mkPosition = (rowIndex: number, colIndex: number): Position => ({
  row: rowIndex + 1,
  column: colIndex + 1,
});

export function saddlePoints(matrix: readonly (readonly number[])[]): Position[] {
  const rowMax = matrix.map((row) => Math.max(...row));

  const colMin = (matrix[0] ?? []).map((_, column) => Math.min(...matrix.map((row) => row[column])));

  const isSaddlePoint = (value: number, rowIndex: number, colIndex: number): boolean =>
    value === rowMax[rowIndex] && value === colMin[colIndex];

  return matrix.flatMap((row, rowIndex) =>
    row.flatMap((value, colIndex) =>
      isSaddlePoint(value, rowIndex, colIndex) ? [mkPosition(rowIndex, colIndex)] : []
    )
  );
}
