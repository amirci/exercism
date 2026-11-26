type Rows = number[][];
type State = { rows: Rows; current: number[] };

const nextRow = (row: readonly number[]): number[] =>
  Array.from(
    { length: row.length + 1 },
    (_, column) => (row[column - 1] ?? 0) + (row[column] ?? 0)
  );

export class Triangle {
  readonly rows: Rows;

  constructor(rowCount: number) {
    this.rows= Array.from({ length: rowCount }).reduce<State>(
      ({ rows, current }) => {
        rows.push(current);
        return { rows, current: nextRow(current) };
      },
      { rows: [], current: [1] }
    ).rows;
  }

  get lastRow(): number[] {
    return this.rows[this.rows.length - 1] ?? [];
  }
}
