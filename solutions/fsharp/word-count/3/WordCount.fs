module WordCount

open System.Text.RegularExpressions

let countWords (phrase: string): Map<string, int> =
    Regex.Matches(phrase.ToLowerInvariant(), "[a-z0-9]+(?:'[a-z0-9]+)?")
    |> Seq.countBy (fun matched -> matched.Value)
    |> Map.ofSeq
