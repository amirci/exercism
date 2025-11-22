import scala.annotation.tailrec

object ArmstrongNumbers {
  def isArmstrongNumber(number: Int): Boolean = {
    val digits = toDigits(number)
    digits
      .map(Math.pow(_, digits.length))
      .sum == number
  }

  private def toDigits(number: Int): List[Int] = {
    @tailrec
    def toDigits(number: Int, acc: List[Int]): List[Int] = {
      if (number == 0) acc
      else toDigits(number / 10, number % 10 +: acc)
    }
    toDigits(number, Nil)
  }
}