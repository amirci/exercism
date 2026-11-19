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
type Bands = readonly [Color, Color, Color, ...Color[]];
type Prefix = readonly [divisor: number, name: string];

const PREFIXES = [
  [1_000_000_000, "giga"],
  [1_000_000, "mega"],
  [1_000, "kilo"],
] as const;

const colorCode = (color: Color): number => COLORS.indexOf(color);

const calculateResistance = ([tens, ones, zeroCount]: Bands): number =>
  (colorCode(tens) * 10 + colorCode(ones)) * 10 ** colorCode(zeroCount);

const prefixFor = (resistance: number): Prefix =>
  PREFIXES.find(([divisor]) => resistance >= divisor) ?? [1, ""];

export function decodedResistorValue(bands: Bands): string {
  const resistance = calculateResistance(bands);
  const [divisor, prefix] = prefixFor(resistance);

  return `${resistance / divisor} ${prefix}ohms`;
}
