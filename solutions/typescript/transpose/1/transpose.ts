export function transpose(lines: readonly string[]): string[] {
  const rows = lines.map((line) => Array.from(line));
  const width = Math.max(0, ...rows.map((row) => row.length));

  const transposeColumn = (column: number): string => {
    let height = rows.length;
    while (height > 0 && rows[height - 1][column] === undefined) {
      height--;
    }

    return rows
      .slice(0, height)
      .map((row) => row[column] ?? " ")
      .join("");
  };

  return Array.from({ length: width }, (_, column) => transposeColumn(column));
}
