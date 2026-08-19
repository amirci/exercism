class Clock private (private val totalMinutes: Int) {
  def +(other: Clock): Clock =
    Clock.fromMinutes(totalMinutes + other.totalMinutes)

  def -(other: Clock): Clock =
    Clock.fromMinutes(totalMinutes - other.totalMinutes)

  override def equals(other: Any): Boolean =
    other match {
      case clock: Clock => totalMinutes == clock.totalMinutes
      case _            => false
    }

  override def hashCode(): Int =
    totalMinutes.hashCode()

  override def toString: String =
    f"${totalMinutes / 60}%02d:${totalMinutes % 60}%02d"
}

object Clock {
  private val MinutesPerDay = 24 * 60

  def apply(hours: Int, minutes: Int): Clock =
    fromMinutes(hours * 60 + minutes)

  def apply(minutes: Int): Clock =
    fromMinutes(minutes)

  private def fromMinutes(minutes: Int): Clock =
    new Clock(Math.floorMod(minutes, MinutesPerDay))
}
