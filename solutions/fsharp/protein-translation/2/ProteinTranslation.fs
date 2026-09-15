module ProteinTranslation

let private proteinByCodon =
    Map [
        "AUG", "Methionine"
        "UUU", "Phenylalanine"
        "UUC", "Phenylalanine"
        "UUA", "Leucine"
        "UUG", "Leucine"
        "UCU", "Serine"
        "UCC", "Serine"
        "UCA", "Serine"
        "UCG", "Serine"
        "UAU", "Tyrosine"
        "UAC", "Tyrosine"
        "UGU", "Cysteine"
        "UGC", "Cysteine"
        "UGG", "Tryptophan"
    ]

let private splitIntoCodons (rna: string) =
    rna
    |> Seq.chunkBySize 3
    |> Seq.map System.String

let private isNotStopCodon codon =
    not (Set.contains codon (Set [ "UAA"; "UAG"; "UGA" ]))

let proteins (rna: string): string list =
    rna
    |> splitIntoCodons
    |> Seq.takeWhile isNotStopCodon
    |> Seq.choose (fun codon -> Map.tryFind codon proteinByCodon)
    |> Seq.toList
