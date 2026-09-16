module HighScores

type Scores = int list

let scores (values: Scores) = values

let latest (values: Scores) = List.last values

let personalBest (values: Scores) = List.max values

let personalTopThree (values: Scores): Scores =
    values |> List.sortDescending |> List.truncate 3
