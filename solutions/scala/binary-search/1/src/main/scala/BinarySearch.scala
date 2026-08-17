import scala.annotation.tailrec

object BinarySearch {
  def find(items: List[Int], item: Int): Option[Int] =
    @tailrec
    def search(left: Int, right: Int): Option[Int] =
      if left > right then None
      else
        val middle = left + (right - left) / 2

        items(middle) match
          case value if value == item => Some(middle)
          case value if value < item => search(middle + 1, right)
          case _ => search(left, middle - 1)

    search(0, items.length - 1)
}
