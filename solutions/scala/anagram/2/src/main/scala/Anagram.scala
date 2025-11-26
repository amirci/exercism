object Anagram {

  def findAnagrams(word: String, candidates: Seq[String]): Seq[String] = {
    val lower = word.toLowerCase
    val sorted = lower.sorted
    candidates.filter(_.toLowerCase != lower).filter(_.toLowerCase.sorted == sorted)
  }

}