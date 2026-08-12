object ProteinTranslation {
  private val Stop = "STOP"

  private val proteinsByCodon = Map(
    "AUG" -> "Methionine",
    "UUU" -> "Phenylalanine",
    "UUC" -> "Phenylalanine",
    "UUA" -> "Leucine",
    "UUG" -> "Leucine",
    "UCU" -> "Serine",
    "UCC" -> "Serine",
    "UCA" -> "Serine",
    "UCG" -> "Serine",
    "UAU" -> "Tyrosine",
    "UAC" -> "Tyrosine",
    "UGU" -> "Cysteine",
    "UGC" -> "Cysteine",
    "UGG" -> "Tryptophan",
    "UAA" -> Stop,
    "UAG" -> Stop,
    "UGA" -> Stop
  )

  def proteins(rna: String): Seq[String] =
    rna
      .grouped(3)
      .map(proteinsByCodon)
      .takeWhile(_ != Stop)
      .toSeq
}
