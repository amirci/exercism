//
const nextRow = (previous) => {
  if (previous.length === 0) return [1];

  const innerValues = previous.slice(1).reduce((values, right, index) => {
    values.push(previous[index] + right);
    return values;
  }, []);

  return [
    1,
    ...innerValues,
    1,
  ];
};

export const rows = (numRows) =>
  Array.from({ length: numRows }).reduce((triangle) => {
    const previous = triangle.at(-1) ?? [];
    return [...triangle, nextRow(previous)];
  }, []);
