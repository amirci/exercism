class Clock private (private val totalMinutes: Int) {
  def +(other: Clock): Clock =
    Clock(totalMinutes + other.totalMinutes)

  def -(other: Clock): Clock =
    Clock(totalMinutes - other.totalMinutes)

  override def equals(other: Any): Boolean =
    other match {
      case clock: Clock => totalMinutes == clock.totalMinutes
      case _            => false
    }

  override def hashCode(): Int =
    totalMinutes.hashCode()

  override def toString: String =
    f"${totalMinutes / Clock.MinutesPerHour}%02d:${totalMinutes % Clock.MinutesPerHour}%02d"
}

object Clock {
  private val MinutesPerHour = 60
  private val MinutesPerDay = 24 * MinutesPerHour

  def apply(hours: Int, minutes: Int): Clock =
    Clock(hours * 60 + minutes)

  def apply(minutes: Int): Clock =
    new Clock(Math.floorMod(minutes, MinutesPerDay))
}
