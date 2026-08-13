object RotationalCipher {
  private val AlphabetSize = 26

  def rotate(text: String, shift: Int): String =
    text.map(rotate(_, shift))

  private def rotate(character: Char, shift: Int): Char =
    character match {
      case c if c >= 'a' && c <= 'z' => rotateFrom(c, 'a', shift)
      case c if c >= 'A' && c <= 'Z' => rotateFrom(c, 'A', shift)
      case c => c
    }

  private def rotateFrom(character: Char, base: Char, shift: Int): Char =
    val offset = (character - base + shift) % AlphabetSize

    (base + offset).toChar
}
