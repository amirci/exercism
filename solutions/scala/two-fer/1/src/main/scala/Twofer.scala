object Twofer {
  def twofer(name: String = null): String = s"One for ${Option(name).getOrElse("you")}, one for me."
}
