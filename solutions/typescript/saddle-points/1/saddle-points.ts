type Position = { row: number; column: number };

export function saddlePoints(matrix: readonly (readonly number[])[]): Position[] {
  const rowMaxima = matrix.map((row) => Math.max(...row));
  const columnMinima = (matrix[0] ?? []).map((_, column) =>
    Math.min(...matrix.map((row) => row[column]))
  );

  return matrix.flatMap((row, rowIndex) =>
    row.flatMap((value, columnIndex) =>
      value === rowMaxima[rowIndex] && value === columnMinima[columnIndex]
        ? [{ row: rowIndex + 1, column: columnIndex + 1 }]
        : []
    )
  );
}
