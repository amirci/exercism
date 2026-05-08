sealed trait Animal:
  def name: String

private case class Fly(name: String) extends Animal

private class Rhyme(val name: String, val rhyme: String) extends Animal

private case class ExtraRhyme(override val name: String, override val rhyme: String) extends Rhyme(name, rhyme)

private case class Horse(name: String, deadLine: String) extends Animal

private val animals: Array[Animal] = Array(
  Fly("fly"),
  ExtraRhyme("spider","It wriggled and jiggled and tickled inside her."),
  Rhyme("bird","How absurd to swallow a bird!"),
  Rhyme("cat","Imagine that, to swallow a cat!"),
  Rhyme("dog","What a hog, to swallow a dog!"),
  Rhyme("goat","Just opened her throat and swallowed a goat!"),
  Rhyme("cow","I don't know how she swallowed a cow!"),
  Horse("horse","She's dead, of course!")
)

private val flyReason = "I don't know why she swallowed the fly. Perhaps she'll die."

private def openingLine(animal: Animal) = s"I know an old lady who swallowed a ${animal.name}."

private def optionalRhymeLine(animal: Animal): String = animal match {
  case rhyme: Rhyme => rhyme.rhyme
  case _ => ""
}

private def catchPhrase(swallower: Animal, caught: Animal): String = {
  val rhyme = caught match {
    case extra: ExtraRhyme => extra.rhyme.replace("It", " that")
    case _ => "."
  }
  s"She swallowed the ${swallower.name} to catch the ${caught.name}$rhyme"
}

private def catchLines(swallowedAnimals: Array[Animal]): List[String] = {
  swallowedAnimals
    .reverse
    .sliding(2)
    .collect { case Array(swallower, caught) => catchPhrase(swallower, caught) }
    .toList
}

object FoodChain {

  private def verse(number: Int): String = {
    val target = animals(number - 1)

    val body = target match {
      case Horse(_, deadLine) => List(deadLine)
      case _ => optionalRhymeLine(target) +: catchLines(animals.take(number)) :+ flyReason
    }

    (openingLine(target) +: body.filter(s => s.nonEmpty)).mkString("", "\n", "\n")
  }

  def recite(startAt: Int, endAt: Int): String = {
    (startAt to endAt)
      .map(verse)
      .mkString("", "\n", "\n")
  }


}
