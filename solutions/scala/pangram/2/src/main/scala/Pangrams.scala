object Pangrams {
  def isPangram(phrase: String) = {
    "[a-z]"
      .r
      .findAllIn(phrase.toLowerCase)
      .toList
      .distinct
      .length == 26
  }
}

