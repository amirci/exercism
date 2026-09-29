const colors = [
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

type Color = typeof colors[number];

export function decodedValue(bands: readonly Color[]): number {
  return Number(
    bands
      .slice(0, 2)
      .map((band) => colors.indexOf(band))
      .join(""),
  );
}
