export function ofSize(size: number): number[][] {
  const matrix = Array.from({ length: size }, () => Array<number>(size).fill(0));
  let top = 0;
  let bottom = size - 1;
  let left = 0;
  let right = size - 1;
  let value = 1;

  while (top <= bottom && left <= right) {
    for (let column = left; column <= right; column++) {
      matrix[top][column] = value++;
    }
    top++;

    for (let row = top; row <= bottom; row++) {
      matrix[row][right] = value++;
    }
    right--;

    for (let column = right; column >= left; column--) {
      matrix[bottom][column] = value++;
    }
    bottom--;

    for (let row = bottom; row >= top; row--) {
      matrix[row][left] = value++;
    }
    left++;
  }

  return matrix;
}
