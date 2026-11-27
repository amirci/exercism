private case class NormalizedMinutes(value: Int)

case class Clock private (private val time: NormalizedMinutes):
  def +(that: Clock): Clock = Clock(time.value + that.time.value)

  def -(that: Clock): Clock = Clock(time.value - that.time.value)

object Clock:
  private val MinutesPerHour = 60
  private val MinutesPerDay = 24 * MinutesPerHour

  def apply(hours: Int, minutes: Int): Clock =
    Clock(hours * MinutesPerHour + minutes)

  def apply(minutes: Int): Clock =
    new Clock(NormalizedMinutes(Math.floorMod(minutes, MinutesPerDay)))
