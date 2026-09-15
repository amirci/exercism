module Raindrops

let convert (number: int): string =
    let sounds = [ (3, "Pling"); (5, "Plang"); (7, "Plong") ]

    let result =
        sounds
        |> List.choose (fun (factor, sound) ->
            if number % factor = 0 then Some sound else None)
        |> String.concat ""

    if result = "" then string number else result
