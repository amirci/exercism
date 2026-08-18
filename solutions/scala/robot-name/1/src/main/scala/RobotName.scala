import scala.util.Random

class Robot {
  private var currentName: String = Robot.nextName()

  def name: String =
    currentName

  def reset(): Unit =
    currentName = Robot.nextName()
}

object Robot {
  private val PossibleNames =
    for
      first <- 'A' to 'Z'
      second <- 'A' to 'Z'
      number <- 0 to 999
    yield f"$first$second$number%03d"

  private val names =
    Random.shuffle(PossibleNames).iterator

  private def nextName(): String =
    names.next()
}
