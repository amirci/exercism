module WordCount

open System.Text.RegularExpressions

let private incrementCount counts word =
    let count = counts |> Map.tryFind word |> Option.defaultValue 0
    Map.add word (count + 1) counts

let countWords (phrase: string): Map<string, int> =
    Regex.Matches(phrase.ToLowerInvariant(), "[a-z0-9]+(?:'[a-z0-9]+)?")
    |> Seq.cast<Match>
    |> Seq.map (fun matched -> matched.Value)
    |> Seq.fold incrementCount Map.empty
