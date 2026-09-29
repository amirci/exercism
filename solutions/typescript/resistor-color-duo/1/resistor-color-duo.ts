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

export function decodedValue(bands: readonly string[]): number {
  return Number(
    bands
      .slice(0, 2)
      .map((band) => colors.indexOf(band as (typeof colors)[number]))
      .join(""),
  );
}
