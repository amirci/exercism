object KillerSudokuHelper {
  private val Digits = 1 to 9

  def combinations(sum: Int, size: Int, exclude: List[Int]): List[List[Int]] =
    Digits
      .filterNot(exclude.contains)
      .toList
      .combinations(size)
      .filter(_.sum == sum)
      .toList
}
