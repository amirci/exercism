object Luhn:
  def valid(input: String): Boolean =
    val digits = input.filterNot(_.isWhitespace)

    digits.length > 1 &&
      digits.forall(_.isDigit) &&
      checksum(digits.map(_.asDigit)) % 10 == 0

  private def checksum(digits: Seq[Int]): Int =
    digits
      .reverse
      .zipWithIndex
      .map(luhnDigit)
      .sum

  private def luhnDigit(digitWithIndex: (Int, Int)): Int =
    val (digit, index) = digitWithIndex

    if index % 2 == 1 then doubleDigit(digit) else digit

  private def doubleDigit(digit: Int): Int =
    val doubled = digit * 2
    if doubled > 9 then doubled - 9 else doubled
