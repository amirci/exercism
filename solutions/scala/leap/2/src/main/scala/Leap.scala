object Leap {
  def leapYear(year: Int): Boolean =
    def isDivisibleBy(divisor: Int): Boolean = year % divisor == 0

    isDivisibleBy(4) && (!isDivisibleBy(100) || isDivisibleBy(400))
}
