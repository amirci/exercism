const nextRow = (row: readonly number[]): number[] =>
  Array.from(
    { length: row.length + 1 },
    (_, column) => (row[column - 1] ?? 0) + (row[column] ?? 0)
  );

export class Triangle {
  readonly rows: number[][] = [];

  constructor(rowCount: number) {
    let row = [1];

    for (let index = 0; index < rowCount; index++) {
      this.rows.push(row);
      if (index + 1 < rowCount) {
        row = nextRow(row);
      }
    }
  }

  get lastRow(): number[] {
    return this.rows[this.rows.length - 1] ?? [];
  }
}
