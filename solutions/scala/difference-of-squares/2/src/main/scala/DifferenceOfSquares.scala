object DifferenceOfSquares {

  def sumOfSquares(n: Int): Int =
    n * (n + 1) * (2 * n + 1) / 6
    // (1 to n).map(number => number * number).sum

  def squareOfSum(n: Int): Int =
    val theSum = n * (n + 1) / 2
    // val theSum = (1 to n).sum
    theSum * theSum

  def differenceOfSquares(n: Int): Int =
    squareOfSum(n) - sumOfSquares(n)
}
