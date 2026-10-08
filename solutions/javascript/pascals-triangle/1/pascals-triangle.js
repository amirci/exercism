//
const nextRow = (previous) => {
  let topLeft = 0;
  return [
    ...previous.map((topRight) => {
      const num = topLeft + topRight;
      topLeft = topRight;
      return num;
    }),
    1,
  ];
};

export const rows = (numRows) => {
  let currRow = [];
  return Array.from({ length: numRows }, () => {
    currRow = nextRow(currRow);
    return currRow;
  });
};
