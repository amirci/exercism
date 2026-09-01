object Diamond:
  def rows(letter: Char): List[String] =
    val topRows = ('A' to letter).map(row(letter)).toList
    topRows ++ topRows.init.reverse

  private def row(lastLetter: Char)(letter: Char): String =
    val outerSpaces = " " * (lastLetter - letter)

    if letter == 'A' then
      outerSpaces + letter + outerSpaces
    else
      val innerSpaces = " " * (2 * (letter - 'A') - 1)
      outerSpaces + letter + innerSpaces + letter + outerSpaces
