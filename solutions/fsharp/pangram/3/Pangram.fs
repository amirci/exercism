module Pangram

let isPangram (input: string): bool =
    let alphabet = set ['a' .. 'z']

    input.ToLowerInvariant()
    |> set
    |> Set.isSubset alphabet
