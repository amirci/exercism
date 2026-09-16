module Hamming

let distance (strand1: string) (strand2: string): int option =
    if String.length strand1 <> String.length strand2 then
        None
    else
        strand1
        |> Seq.zip strand2
        |> Seq.filter (fun (a, b) -> a <> b)
        |> Seq.length
        |> Some
