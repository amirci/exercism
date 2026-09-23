module TisburyTreasureHunt

let getCoordinate (_, coordinate): string =
    coordinate

let convertCoordinate (coordinate: string): int * char =
    (int (string coordinate[0]), coordinate[1])

let compareRecords
    (azarasData: string * string)
    (ruisData: string * (int * char) * string)
    : bool =
    let (_, coordinate, _) = ruisData
    convertCoordinate (getCoordinate azarasData) = coordinate

let createRecord
    (azarasData: string * string)
    (ruisData: string * (int * char) * string)
    : string * string * string * string =
    let (treasure, coordinate) = azarasData
    let (location, ruisCoordinate, quadrant) = ruisData

    if convertCoordinate coordinate = ruisCoordinate then
        (coordinate, location, quadrant, treasure)
    else
        ("", "", "", "")
