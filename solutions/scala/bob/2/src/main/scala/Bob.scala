object Bob {
  def response(msg: String): String = {
    val trimmed = msg.trim

    if trimmed.isEmpty then
      return "Fine. Be that way!"

    val isShouting = trimmed.exists(_.isLetter) && trimmed == trimmed.toUpperCase
    val isQuestion = trimmed.endsWith("?")

    (isShouting, isQuestion) match {
      case (true, true) => "Calm down, I know what I'm doing!"
      case (true, false) => "Whoa, chill out!"
      case (false, true) => "Sure."
      case _ => "Whatever."
    }
  }
}
