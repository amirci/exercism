case class Bowling(
    rolls: Vector[Int] = Vector.empty,
    frames: Vector[Vector[Int]] = Vector.empty,
    currentFrame: Vector[Int] = Vector.empty,
    invalid: Boolean = false,
    complete: Boolean = false,
) {
  def roll(pins: Int): Bowling =
    if (invalid || complete) copy(invalid = true)
    else if (pins < 0 || pins > 10) copy(invalid = true)
    else if (frames.length < 9) rollRegular(pins)
    else rollFinal(pins)

  def score(): Either[String, Int] =
    if (invalid) Left("Invalid game")
    else if (!complete) Left("Game is incomplete")
    else Right(scoreFrames(rolls, 0, 0))

  private def rollRegular(pins: Int): Bowling = currentFrame match {
    case Vector() if pins == 10 =>
      copy(rolls = rolls :+ pins, frames = frames :+ Vector(pins))
    case Vector() =>
      copy(rolls = rolls :+ pins, currentFrame = Vector(pins))
    case Vector(first) if first + pins <= 10 =>
      copy(
        rolls = rolls :+ pins,
        frames = frames :+ Vector(first, pins),
        currentFrame = Vector.empty,
      )
    case _ => copy(invalid = true)
  }

  private def rollFinal(pins: Int): Bowling = currentFrame match {
    case Vector() =>
      copy(rolls = rolls :+ pins, currentFrame = Vector(pins))
    case Vector(first) if first == 10 || first + pins == 10 =>
      copy(rolls = rolls :+ pins, currentFrame = Vector(first, pins))
    case Vector(first) if first + pins < 10 =>
      copy(
        rolls = rolls :+ pins,
        frames = frames :+ Vector(first, pins),
        currentFrame = Vector.empty,
        complete = true,
      )
    case Vector(first, second) if first == 10 && second < 10 && second + pins > 10 =>
      copy(invalid = true)
    case Vector(first, second) =>
      copy(
        rolls = rolls :+ pins,
        frames = frames :+ Vector(first, second, pins),
        currentFrame = Vector.empty,
        complete = true,
      )
    case _ => copy(invalid = true)
  }

  private def scoreFrames(
      allRolls: Vector[Int],
      index: Int,
      frame: Int,
  ): Int =
    if (frame == 10) 0
    else if (allRolls(index) == 10) {
      10 + allRolls(index + 1) + allRolls(index + 2) +
        scoreFrames(allRolls, index + 1, frame + 1)
    } else if (allRolls(index) + allRolls(index + 1) == 10) {
      10 + allRolls(index + 2) +
        scoreFrames(allRolls, index + 2, frame + 1)
    } else {
      allRolls(index) + allRolls(index + 1) +
        scoreFrames(allRolls, index + 2, frame + 1)
    }
}
