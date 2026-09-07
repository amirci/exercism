object Alphametics {
  private type Digit = Int
  private type Assignments = Map[Char, Digit]
  private type UsedDigits = Set[Digit]

  def solve(input: String): Option[Assignments] =
    parsePuzzle(input).flatMap(puzzle => solveColumns(puzzle, puzzle.columns))

  private case class Puzzle(columns: List[Column], letters: Set[Char], leadingLetters: Set[Char])

  private case class Column(addends: List[Char], result: Char)

  private object Column {
    def from(addends: List[String], result: String, column: Int): Column =
      Column(addends.flatMap(letterAt(_, column)), result(result.length - column - 1))
  }

  private def parsePuzzle(input: String): Option[Puzzle] =
    def parseParts(): Option[(List[String], String)] =
      input.split(" == ").toList match
        case left :: result :: Nil =>
          Some((left.split(" \\+ ").toList, result))
        case _ =>
          None

    def validWords(addends: List[String], result: String): Option[List[String]] =
      val words = addends :+ result
      val letters = words.flatten.toSet

      Option.when(
        words.forall(_.nonEmpty) &&
          addends.forall(_.length <= result.length) &&
          letters.size <= 10
      )(words)

    def toPuzzle(addends: List[String], result: String, words: List[String]): Puzzle =
      Puzzle(
        columns = (0 until words.map(_.length).max).toList.map(Column.from(addends, result, _)),
        letters = words.flatten.toSet,
        leadingLetters = words.filter(_.length > 1).map(_.head).toSet
      )

    for
      (addends, result) <- parseParts()
      words <- validWords(addends, result)
    yield toPuzzle(addends, result, words)


  private case class SolverState(carry: Int = 0, assignments: Assignments = Map(), usedDigits: UsedDigits = Set())

  private def solveColumns(puzzle: Puzzle, columns: List[Column], state: SolverState = SolverState()): Option[Assignments] =
    columns match
      case Nil if state.carry != 0 => None
      case Nil => Some(state.assignments)
      case column :: remaining =>
        assignAddendDigits(puzzle, column.addends, column.result, remaining, state, sum = 0)

  private def assignAddendDigits(puzzle: Puzzle, addends: List[Char], result: Char, remaining: List[Column], state: SolverState, sum: Int): Option[Assignments] =
    addends match
      case Nil => assignResultDigit(puzzle, result, remaining, state, sum)
      case letter :: rest => assignAddendDigit(puzzle, letter, rest, result, remaining, state, sum)

  private def assignAddendDigit(puzzle: Puzzle, letter: Char, rest: List[Char], result: Char, remaining: List[Column], state: SolverState, sum: Int): Option[Assignments] =
    state.assignments.get(letter) match
      case Some(digit) =>
        assignAddendDigits(puzzle, rest, result, remaining, state, sum + digit)
      case None =>
        availableDigits(letter, puzzle, state.usedDigits).iterator
          .flatMap { digit =>
            assignAddendDigits(
              puzzle,
              rest,
              result,
              remaining,
              state.copy(assignments = state.assignments + (letter -> digit), usedDigits = state.usedDigits + digit),
              sum + digit
            )
          }
          .nextOption()

  private def assignResultDigit(puzzle: Puzzle, letter: Char, remaining: List[Column], state: SolverState, sum: Int): Option[Assignments] =
    val total = sum + state.carry
    val digit = total % 10
    val nextCarry = total / 10

    state.assignments.get(letter) match
      case Some(assignedDigit) if assignedDigit == digit =>
        solveColumns(puzzle, remaining, state.copy(carry = nextCarry))
      case Some(_) =>
        None
      case None if state.usedDigits.contains(digit) || isLeadingZero(letter, digit, puzzle) =>
        None
      case None =>
        solveColumns(
          puzzle,
          remaining,
          state.copy(
            carry = nextCarry,
            assignments = state.assignments + (letter -> digit),
            usedDigits = state.usedDigits + digit
          )
        )

  private def availableDigits(letter: Char, puzzle: Puzzle, usedDigits: UsedDigits): Iterable[Digit] =
    (0 to 9).filterNot(digit => usedDigits.contains(digit) || isLeadingZero(letter, digit, puzzle))

  private def isLeadingZero(letter: Char, digit: Digit, puzzle: Puzzle): Boolean =
    digit == 0 && puzzle.leadingLetters.contains(letter)

  private def letterAt(word: String, column: Int): Option[Char] =
    val index = word.length - column - 1

    Option.when(index >= 0)(word(index))
}
