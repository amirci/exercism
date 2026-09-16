module HighScores

type Scores = int list

let scores = id

let latest = List.last

let personalBest = List.max

let personalTopThree: Scores -> Scores =
    List.sortDescending >> List.truncate 3
