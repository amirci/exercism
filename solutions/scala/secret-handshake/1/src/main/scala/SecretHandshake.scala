object SecretHandshake {
  private val Reverse = 16

  private val actions = List(
    1 -> "wink",
    2 -> "double blink",
    4 -> "close your eyes",
    8 -> "jump"
  )

  def commands(number: Int): List[String] =
    val selectedActions =
      actions
        .collect { case (bit, action) if hasBit(number, bit) => action }

    if hasBit(number, Reverse) then selectedActions.reverse
    else selectedActions

  private def hasBit(number: Int, bit: Int): Boolean =
    (number & bit) != 0
}
