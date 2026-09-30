const COLORS = [
  "black",
  "brown",
  "red",
  "orange",
  "yellow",
  "green",
  "blue",
  "violet",
  "grey",
  "white",
] as const;

type Color = typeof COLORS[number];

const PREFIXES = ["", "kilo", "mega", "giga"] as const;

const colorCode = (color: Color): number => COLORS.indexOf(color);

export function decodedResistorValue(
  [tens, ones, zeroCount]: readonly [Color, Color, Color, ...Color[]],
): string {
  const value = colorCode(tens) * 10 + colorCode(ones);
  const zeros = colorCode(zeroCount);
  let displayValue = value * 10 ** zeros;
  let prefixIndex = 0;

  while (displayValue >= 1000) {
    displayValue /= 1000;
    prefixIndex += 1;
  }

  return `${displayValue} ${PREFIXES[prefixIndex]}ohms`;
}
