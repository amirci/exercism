import scala.annotation.tailrec

object ReverseString {
  def reverse(str: String): String = reverseIt(str, Nil)

  @tailrec
  private def reverseIt(pending: String, reversed: List[Char]): String = pending match {
    case "" => reversed.mkString
    case _ => reverseIt(pending.tail, pending.head :: reversed)
  }
}
