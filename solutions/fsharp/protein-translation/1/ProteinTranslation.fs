module ProteinTranslation

let private translateCodon = function
    | "AUG" -> Some "Methionine"
    | "UUU" | "UUC" -> Some "Phenylalanine"
    | "UUA" | "UUG" -> Some "Leucine"
    | "UCU" | "UCC" | "UCA" | "UCG" -> Some "Serine"
    | "UAU" | "UAC" -> Some "Tyrosine"
    | "UGU" | "UGC" -> Some "Cysteine"
    | "UGG" -> Some "Tryptophan"
    | "UAA" | "UAG" | "UGA" -> None
    | _ -> None

let proteins (rna: string): string list =
    rna
    |> Seq.chunkBySize 3
    |> Seq.map (fun codon -> System.String codon)
    |> Seq.map translateCodon
    |> Seq.takeWhile Option.isSome
    |> Seq.choose id
    |> Seq.toList
