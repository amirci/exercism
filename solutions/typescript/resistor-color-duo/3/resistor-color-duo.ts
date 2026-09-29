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

const colorCode = (color: Color) => COLORS.indexOf(color);

export function decodedValue([tens, ones]: readonly Color[]): number {
  return colorCode(tens) * 10 + colorCode(ones);
}
