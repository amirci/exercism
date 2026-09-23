module TisburyTreasureHunt

type Treasure = string * string
type Coordinate = int * char
type Location = string * Coordinate * string
type MatchedRecord = string * string * string * string

let getCoordinate = snd

let convertCoordinate (coordinate: string): Coordinate =
    let row, column = coordinate[0], coordinate[1]
    (int (string row), column)

let compareRecords ((_, coordinate): Treasure) ((_, expectedCoordinate, _): Location) : bool =
    coordinate
    |> convertCoordinate
    |> (=) expectedCoordinate

let createRecord
    ((treasure, coordinate): Treasure)
    ((location, expectedCoordinate, quadrant): Location)
    : MatchedRecord =
  (location, expectedCoordinate, quadrant)
  |> compareRecords (treasure, coordinate)
  |> function
     | true -> (coordinate, location, quadrant, treasure)
     | _ -> ("", "", "", "")
