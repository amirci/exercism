object PhoneNumber {

  def clean(nbr: String): Option[String] = {
    Some(removeWhiteSpace(nbr))
      .filter(lengthIsValid)
      .map(removeHeadIfEleven)
      .filter(areAllDigits)
      .filter(areaCodeIsValid)
      .filter(exchangeCodeIsValid)
  }

  private def removeWhiteSpace(nbr: String) = "\\W".r.replaceAllIn(nbr, "")

  private def lengthIsValid(s: String) = s.length == 10 || (s.length == 11 && s.head == '1')

  private def areaCodeIsValid(s: String): Boolean = s.head != '0' && s.head != '1'

  private def exchangeCodeIsValid(s: String): Boolean = s(3) != '0' && s(3) != '1'

  private def areAllDigits(s: String): Boolean = s.forall(_.isDigit)

  private def removeHeadIfEleven(s: String): String = if (s.length == 11) s.tail else s
}
