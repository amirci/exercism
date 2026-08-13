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
      // The collection version below is concise, but timed out in the online runner:
      // val pairedFactors = (2 to root).filter(number % _ == 0).flatMap(factor => pairedFactor(number, factor))
      // (1 +: pairedFactors).sum
      //
      // Keep the sqrt bound, but avoid per-factor collection allocations.
      val root = math.sqrt(number).toInt
      var sum = 1
      var factor = 2

      while factor <= root do
        if number % factor == 0 then
          val pair = number / factor
          sum += factor

          if pair != factor then
            sum += pair

        factor += 1

      sum
}
