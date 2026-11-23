import scala.util.Random

class Robot {
  private var currentName: String = Robot.nextName()

  def name: String =
    currentName

  def reset(): Unit =
    currentName = Robot.nextName()
}

object Robot {
  private val letterCount = 2
  private val digitCount = 3
  private val letterChoices = ('A' to 'Z').toSeq
  private val digitChoices = ('0' to '9').toSeq

  private val generators =
    List.fill(letterCount)(letterChoices) ++ List.fill(digitCount)(digitChoices)

  private val possibleNames = generators.foldLeft(Seq("")) { (prefixes, choices) =>
    for
      prefix <- prefixes
      character <- choices
    yield s"$prefix$character"
  }

  private val names =
    Random.shuffle(possibleNames).iterator

  private def nextName(): String =
    names.next()
}
