type Grid = number[][];

const parseRow = (line: string): number[] =>
  line.trim().split(/\s+/).map(Number);

const transpose = (rows: Grid): Grid =>
  (rows[0] ?? []).map((_, column) => rows.map((row) => row[column]));

export class Matrix {
  readonly rows: Grid;
  readonly columns: Grid;

  constructor(input: string) {
    this.rows = input.trim() === "" ? [] : input.trim().split("\n").map(parseRow);
    this.columns = transpose(this.rows);
  }
}
