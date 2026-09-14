module BirdWatcher

let lastWeek: int[] =
   [| 0; 2; 5; 3; 7; 8; 4; |]

let yesterday(counts: int[]): int =
  counts[counts.Length - 2]

let total(counts: int[]): int =
  counts |> Array.sum

let dayWithoutBirds(counts: int[]): bool =
  counts |> Array.exists(fun d -> d = 0)

let incrementTodaysCount(counts: int[]): int[] =
  let incremented = counts |> Array.last |> (+) 1
  counts |> Array.updateAt (counts.Length - 1) incremented

let private isZero = (=) 0

let private isEven number = number % 2 = 0

let private isOdd = not << isEven

let private evenDays(counts: int[]): int[] =
    counts
    |> Array.indexed
    |> Array.filter (fun (idx, _) -> isOdd idx)
    |> Array.map snd

let private oddDays(counts: int[]): int[] =
    counts
    |> Array.indexed
    |> Array.filter (fun (idx, _) -> isEven idx)
    |> Array.map snd


let private evenDaysAreZero = evenDays >> Array.forall isZero

let private evenDaysAreTen = evenDays >> Array.forall ((=) 10)

let private oddDaysSpottedFive = oddDays >> Array.forall ((=) 5)

let unusualWeek(counts: int[]): bool =
    evenDaysAreZero(counts) ||
    evenDaysAreTen(counts)  ||
    oddDaysSpottedFive(counts)
