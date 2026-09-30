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

const PREFIXES = [
  [1_000_000_000, "giga"],
  [1_000_000, "mega"],
  [1_000, "kilo"],
] as const;

const colorCode = (color: Color): number => COLORS.indexOf(color);

export function decodedResistorValue(
  [tens, ones, zeroCount]: readonly [Color, Color, Color, ...Color[]],
): string {
  const value = colorCode(tens) * 10 + colorCode(ones);
  const zeros = colorCode(zeroCount);
  const resistance = value * 10 ** zeros;
  const [divisor, prefix] =
    PREFIXES.find(([threshold]) => resistance >= threshold) ?? [1, ""];

  return `${resistance / divisor} ${prefix}ohms`;
}
