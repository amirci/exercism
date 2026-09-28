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

private case class CompletedFrame(start: Int, end: Int) // end is inclusive

private case class CurrentFrame(start: Int, rolls: Vector[Roll])

private sealed trait GameState

private case class InProgress(
  rolls: Vector[Roll],
  frames: Vector[CompletedFrame],
  currentFrame: CurrentFrame,
) extends GameState

private case class Complete(
  rolls: Vector[Roll],
  frames: Vector[CompletedFrame],
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
    if isFinalFrame(valid) then rollFinal(valid, pins)
    else rollRegular(valid, pins)

  private def isFinalFrame(game: InProgress): Boolean =
    game.frames.length == 9

  private def rollRegular(game: InProgress, pins: Roll): GameState =
    val CurrentFrame(start, rolls) = game.currentFrame

    rolls match {
      case Vector() if pins.isStrike =>
        nextFrame(game, pins, CompletedFrame(start, start + 2), start + 1)
      case Vector() =>
        addRoll(game, pins)
      case Vector(first) if first + pins <= 10 =>
        val end = start + (if first + pins == 10 then 2 else 1)
        nextFrame(game, pins, CompletedFrame(start, end), start + 2)
      case _ =>
        Invalid("Pin count exceeds pins on the lane")
    }

  private def rollFinal(valid: InProgress, pins: Roll): GameState =
    val CurrentFrame(start, rolls) = valid.currentFrame

    rolls match {
      case Vector() =>
        addRoll(valid, pins)
      case Vector(first) if first.isStrike =>
        addRoll(valid, pins)
      case Vector(first) if first + pins == 10 =>
        addRoll(valid, pins)
      case Vector(first) if first + pins < 10 =>
        finish(valid, pins, CompletedFrame(start, start + 1))
      case Vector(first, second) if first.isStrike && !second.isStrike && second + pins > 10 =>
        Invalid("Pin count exceeds pins on the lane")
      case Vector(_, _) =>
        finish(valid, pins, CompletedFrame(start, start + 2))
      case _ => Invalid("Invalid final frame")
    }

  private def frameRolls(rolls: Vector[Roll])(frame: CompletedFrame): Vector[Roll] =
    rolls.slice(frame.start, frame.end + 1)

  private def addRoll(valid: InProgress, pins: Roll): InProgress =
    InProgress(
      valid.rolls :+ pins,
      valid.frames,
      valid.currentFrame.copy(rolls = valid.currentFrame.rolls :+ pins),
    )

  private def nextFrame(valid: InProgress, pins: Roll, completedFrame: CompletedFrame, nextStart: Int): GameState =
    InProgress(
      valid.rolls :+ pins,
      valid.frames :+ completedFrame,
      CurrentFrame(nextStart, Vector.empty),
    )

  private def finish(valid: InProgress, pins: Roll, completedFrame: CompletedFrame): GameState =
    Complete(valid.rolls :+ pins, valid.frames :+ completedFrame)

}

object Bowling:
  def apply(): Bowling =
    new Bowling(InProgress(Vector.empty, Vector.empty, CurrentFrame(0, Vector.empty)))
