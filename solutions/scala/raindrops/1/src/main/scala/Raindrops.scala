object Raindrops {
  private val Sounds =
    List(3 -> "Pling", 5 -> "Plang", 7 -> "Plong")

  def convert(n: Int): String =
    val sounds =
      Sounds
        .collect { case (factor, sound) if n % factor == 0 => sound }
        .mkString

    if sounds.isEmpty then n.toString else sounds
}
