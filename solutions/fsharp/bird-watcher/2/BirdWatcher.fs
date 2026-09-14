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

let private isEven number = number % 2 = 0

let private isOdd = not << isEven

let private daysOnIndex (matchIndex: int -> bool) (matchValue: int -> bool) (counts: int[]): bool =
    counts
    |> Array.indexed
    |> Array.filter (fst >> matchIndex)
    |> Array.forall (snd >> matchValue)

let private oddIndexedDaysAreZero = daysOnIndex isOdd isZero

let private oddIndexedDaysAreTen = daysOnIndex isOdd ((=) 10)

let private evenIndexedDaysSpottedFive = daysOnIndex isEven ((=) 5)

let unusualWeek(counts: int[]): bool =
    oddIndexedDaysAreZero counts ||
    oddIndexedDaysAreTen counts  ||
    evenIndexedDaysSpottedFive counts
