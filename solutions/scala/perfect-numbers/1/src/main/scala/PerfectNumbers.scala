enum NumberType {
  case Perfect, Abundant, Deficient
}

object PerfectNumbers {
  private val InvalidNumber = "Classification is only possible for natural numbers."

  def classify(number: Int): Either[String, NumberType] =
    if number < 1 then Left(InvalidNumber)
    else Right(number.compareTo(aliquotSum(number)) match {
      case 0 => NumberType.Perfect
      case 1 => NumberType.Deficient
      case -1 => NumberType.Abundant
    })

  private def aliquotSum(number: Int): Int =
    if number == 1 then 0
    else
      val root = math.sqrt(number).toInt
      val pairedFactors = (2 to root).filter(number % _ == 0).flatMap(factor => pairedFactor(number, factor))

      (1 +: pairedFactors).sum

  private def pairedFactor(number: Int, factor: Int): Seq[Int] =
    val pair = number / factor

    if factor == pair then Seq(factor)
    else Seq(factor, pair)
}
