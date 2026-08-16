import scala.annotation.tailrec

object CollatzConjecture {
  def steps(number: Int): Option[Int] =
    Option.when(number > 0)(countSteps(number.toLong, 0))

  @tailrec
  private def countSteps(number: Long, steps: Int): Int =
    number match
      case 1 => steps
      case even if even % 2 == 0 => countSteps(even / 2, steps + 1)
      case odd => countSteps(odd * 3 + 1, steps + 1)
}
