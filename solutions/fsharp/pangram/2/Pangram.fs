module Pangram

let private isAsciiLetter character =
    character >= 'a' && character <= 'z'

let isPangram (input: string): bool =
    let alphabet = set ['a' .. 'z']

    input
    |> Seq.map System.Char.ToLowerInvariant
    |> Seq.filter isAsciiLetter
    |> Set.ofSeq
    |> (=) alphabet
