object RnaTranscription {
  def toRna(dna: String): Option[String] =
    dna
      .map(complement)
      .foldLeft(Option("")) {
        case (Some(rna), Some(nucleotide)) => Some(rna + nucleotide)
        case _ => None
      }

  private def complement(nucleotide: Char): Option[Char] =
    nucleotide match {
      case 'G' => Some('C')
      case 'C' => Some('G')
      case 'T' => Some('A')
      case 'A' => Some('U')
      case _ => None
    }
}
