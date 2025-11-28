object Bob {
  def response(msg: String): String = {
    val trimmed = msg.trim

    val isSilent = (_: String).isEmpty
    def isShouting(s: String) = s.exists(_.isLetter) && s == s.toUpperCase
    def isQuestion(s: String) = s.endsWith("?")

    trimmed match {
      case s if isSilent(s) => "Fine. Be that way!"
      case s if isShouting(s) && isQuestion(s) => "Calm down, I know what I'm doing!"
      case s if isShouting(s) => "Whoa, chill out!"
      case s if isQuestion(s) => "Sure."
      case _ => "Whatever."
    }
  }
}
