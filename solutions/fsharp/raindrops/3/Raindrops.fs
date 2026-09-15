module Raindrops

let private soundIfDivisible number (factor, sound) =
    if number % factor = 0 then Some sound else None

let private sounds = [ (3, "Pling"); (5, "Plang"); (7, "Plong") ]

let convert (number: int): string =
    sounds
    |> List.choose (soundIfDivisible number)
    |> function
        | [] -> string number
        | matchingSounds -> String.concat "" matchingSounds
