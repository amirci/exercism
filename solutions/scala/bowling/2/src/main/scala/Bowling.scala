opaque type Roll = Int

object Roll:
  def from(value: Int): Either[String, Roll] =
    if value >= 0 && value <= 10 then Right(value)
    else Left("Invalid roll")

  extension (roll: Roll)
    def value: Int = roll
    def isStrike: Boolean = roll == 10
    def +(other: Roll): Int = roll + other

import Roll.*

private case class Frame(start: Int, end: Int) // end is inclusive

private sealed trait GameState

private case class InProgress(
    rolls: Vector[Roll],
    frames: Vector[Frame],
    currentFrame: Frame,
) extends GameState

private case class Complete(
    rolls: Vector[Roll],
    frames: Vector[Frame],
) extends GameState

private case class Invalid(reason: String) extends GameState

case class Bowling private (state: GameState) {
  def roll(pins: Int): Bowling = state match {
    case _: Invalid => this
    case _: Complete => new Bowling(Invalid("Cannot roll after game is over"))
    case inProgress: InProgress =>
      val nextState = Roll.from(pins).fold(Invalid.apply, rollCurrentFrame(inProgress, _))
      new Bowling(nextState)
  }

  def score(): Either[String, Int] = state match {
    case Invalid(reason) => Left(reason)
    case _: InProgress => Left("Game is incomplete")
    case complete: Complete =>
      Right(complete.frames.flatMap(frameRolls(complete.rolls)).map(_.value).sum)
  }

  private def rollCurrentFrame(valid: InProgress, pins: Roll): GameState =
    if valid.frames.length < 9 then rollRegular(valid, pins)
    else rollFinal(valid, pins)

  private def rollRegular(valid: InProgress, pins: Roll): GameState =
    val frame = valid.currentFrame
    val count = valid.rolls.length - frame.start
    val currentRolls = frameRolls(valid.rolls)(frame)

    count match {
      case 0 if pins.isStrike =>
        nextFrame(valid, pins, frame.copy(end = frame.start + 2), frame.start + 1)
      case 0 =>
        continue(valid, pins, frame.copy(end = frame.start))
      case 1 if currentRolls(0) + pins <= 10 =>
        val first = currentRolls(0)
        val end = if first + pins == 10 then frame.start + 2 else frame.start + 1
        nextFrame(valid, pins, frame.copy(end = end), frame.start + 2)
      case _ => Invalid("Pin count exceeds pins on the lane")
    }

  private def rollFinal(valid: InProgress, pins: Roll): GameState =
    val frame = valid.currentFrame
    val currentRolls = frameRolls(valid.rolls)(frame)

    currentRolls match {
      case Vector() =>
        val end = if pins.isStrike then frame.start + 2 else frame.start
        continue(valid, pins, frame.copy(end = end))
      case Vector(first) if first.isStrike =>
        continue(valid, pins, frame.copy(end = frame.start + 2))
      case Vector(first) if first + pins <= 10 && first + pins == 10 =>
        continue(valid, pins, frame.copy(end = frame.start + 2))
      case Vector(first) if first + pins < 10 =>
        finish(valid, pins, frame.copy(end = frame.start + 1))
      case Vector(first, second) if first.isStrike && !second.isStrike && second + pins > 10 =>
        Invalid("Pin count exceeds pins on the lane")
      case Vector(_, _) =>
        finish(valid, pins, frame.copy(end = frame.start + 2))
      case _ => Invalid("Invalid final frame")
    }

  private def frameRolls(rolls: Vector[Roll])(frame: Frame): Vector[Roll] =
    rolls.slice(frame.start, frame.end + 1)

  private def continue(valid: InProgress, pins: Roll, currentFrame: Frame): GameState =
    InProgress(valid.rolls :+ pins, valid.frames, currentFrame)

  private def nextFrame(
      valid: InProgress,
      pins: Roll,
      completedFrame: Frame,
      nextStart: Int,
  ): GameState =
    InProgress(
      valid.rolls :+ pins,
      valid.frames :+ completedFrame,
      Frame(nextStart, nextStart - 1),
    )

  private def finish(valid: InProgress, pins: Roll, completedFrame: Frame): GameState =
    Complete(valid.rolls :+ pins, valid.frames :+ completedFrame)

}

object Bowling:
  def apply(): Bowling =
    new Bowling(InProgress(Vector.empty, Vector.empty, Frame(0, -1)))
