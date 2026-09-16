module Pangram

let isPangram (input: string): bool =
    let alphabet = set ['a' .. 'z']

    let letters =
        input
        |> Seq.map System.Char.ToLowerInvariant
        |> Seq.filter (fun character -> character >= 'a' && character <= 'z')
        |> Set.ofSeq

    Set.isSubset alphabet letters
