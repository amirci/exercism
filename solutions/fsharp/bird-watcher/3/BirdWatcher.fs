module BirdWatcher

let lastWeek: int[] =
   [| 0; 2; 5; 3; 7; 8; 4; |]

let yesterday(counts: int[]): int =
  counts[counts.Length - 2]

let total: int[] -> int = Array.sum

let private isZero = (=) 0

let dayWithoutBirds = Array.exists isZero

let incrementTodaysCount(counts: int[]): int[] =
  let incremented = counts |> Array.last |> (+) 1
  counts |> Array.updateAt (counts.Length - 1) incremented

let unusualWeek(counts: int[]): bool =
  match counts with
  | [| _; 0; _; 0; _; 0; _ |] -> true
  | [| _; 10; _; 10; _; 10; _ |] -> true
  | [| 5; _; 5; _; 5; _; 5 |] -> true
  | _ -> false
