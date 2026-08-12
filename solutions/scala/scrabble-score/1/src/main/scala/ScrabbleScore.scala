object ScrabbleScore {
  private val letterScores =
    Map(
      "aeioulnrst" -> 1,
      "dg" -> 2,
      "bcmp" -> 3,
      "fhvwy" -> 4,
      "k" -> 5,
      "jx" -> 8,
      "qz" -> 10
    ).flatMap { case (letters, score) => letters.map(_ -> score) }

  def score(word: String): Int =
    word.toLowerCase.map(letterScores).sum
}
