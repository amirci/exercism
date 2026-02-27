class DNA(seq: String) {

  def nucleotideCounts: Either[String, Map[Char, Int]] = {
    Either.cond(
      allValidChars(seq),
      mapWithZeros ++ seq.groupBy(identity).mapValues(_.size),
      "Invalid DNA sequence"
    )
  }

  private val validNucleotides = "ACGT"

  private def allValidChars(seq:String) = validNucleotides.contains(seq.distinct.sorted)

  private val mapWithZeros = validNucleotides.map(_ -> 0).toMap
}
