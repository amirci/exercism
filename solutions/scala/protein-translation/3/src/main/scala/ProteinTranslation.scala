object ProteinTranslation {
  private val Stop = "STOP"

  def proteins(rna: String): Seq[String] =
    rna
      .grouped(3)
      .map(proteinFor)
      .takeWhile(_ != Stop)
      .toSeq

  private def proteinFor(codon: String): String =
    codon match {
      case "AUG" => "Methionine"
      case "UUU" | "UUC" => "Phenylalanine"
      case "UUA" | "UUG" => "Leucine"
      case "UCU" | "UCC" | "UCA" | "UCG" => "Serine"
      case "UAU" | "UAC" => "Tyrosine"
      case "UGU" | "UGC" => "Cysteine"
      case "UGG" => "Tryptophan"
      case "UAA" | "UAG" | "UGA" => Stop
      case _ => throw new IllegalArgumentException("Invalid codon")
    }
}
