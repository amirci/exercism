
def is_divisible_by(n: Int, target: Int): Boolean = n % target == 0

object Leap {
  def leapYear(year: Int): Boolean = is_divisible_by(year, 4) && (!is_divisible_by(year, 100) || is_divisible_by(year, 400))
}
