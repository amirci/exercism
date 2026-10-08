//
const buildRow = (row, value, index, previous) => [...row, value + (previous[index + 1] ?? 0)];

const nextRow = (previous) => previous.reduce(buildRow, [1]);

export const rows = (numRows) =>
  Array.from({ length: numRows }).reduce((triangle) => {
    const previous = triangle.at(-1) ?? [];
    return [...triangle, nextRow(previous)];
  }, []);
