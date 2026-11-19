export function isLeap(year: number): boolean {
  const isDivisibleBy = (n: number) => year % n == 0;

  return isDivisibleBy(4) && (!isDivisibleBy(100) || isDivisibleBy(400));
}
