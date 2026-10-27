export function annotate(field: string[]): string[] {
  return field.map((row, rowIndex) =>
    [...row].map((cell, columnIndex) => {
      if (cell === "*") return cell;

      let flowers = 0;
      for (let dr = -1; dr <= 1; dr++) {
        for (let dc = -1; dc <= 1; dc++) {
          if (field[rowIndex + dr]?.[columnIndex + dc] === "*") {
            flowers++;
          }
        }
      }

      return flowers === 0 ? " " : String(flowers);
    }).join("")
  );
}
