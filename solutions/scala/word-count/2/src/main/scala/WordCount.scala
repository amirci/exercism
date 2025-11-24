class WordCount(words:String) {

  def countWords = {
    "['|\\w]+"
      .r
      .findAllIn(words.toLowerCase)
      .toList
      .map(removeQuotes)
      .groupBy(identity)
      .map((k, v) => k -> v.length)
  }

  private def removeQuotes(word: String) = if(isQuoted(word)) word.slice(1, word.length - 1) else word

  private def isQuoted(word: String) = word.startsWith("'") && word.endsWith("'")
}
