//
const nextRow = (previous) => {
  return previous
    .map((value, index) => value + (previous[index - 1] ?? 0))
    .concat(1);
};

export const rows = (numRows) =>
  Array.from({ length: numRows }).reduce((triangle) => {
    const previous = triangle.at(-1) ?? [];
    return [...triangle, nextRow(previous)];
  }, []);
