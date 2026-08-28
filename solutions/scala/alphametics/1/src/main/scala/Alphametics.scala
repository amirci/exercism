object Alphametics {
  private type Digit = Int
  private type Assignments = Map[Char, Digit]
  private type UsedDigits = Set[Digit]
  private type Addends = List[String]
  private type Words = List[String]

  def solve(puzzle: String): Option[Assignments] =
    tryParseValidPuzzle(puzzle).flatMap(puzzle => solveColumns(puzzle))

  private case class Puzzle(columns: List[Column], letters: Set[Char], leadingLetters: Set[Char])

  private case class Column(addends: List[Char], result: Char)

  private def tryParseValidPuzzle(input: String): Option[Puzzle] =
    val Array(left, result) = input.split(" == ")
    val addends = left.split(" \\+ ").toList

    if(result.length < addends.map(_.length).max)
      return None

    val words = addends :+ result
    val letters = words.flatten.toSet

    if(letters.size > 10)
      return None

    val maxLength = words.map(_.length).max
    val columns =
      (0 until maxLength).toList.map { column =>
        Column(addends.flatMap(letterAt(_, column)), result(result.length - column - 1))
      }

    Some(
      Puzzle(
        columns = columns,
        letters = words.flatten.toSet,
        leadingLetters = words.filter(_.length > 1).map(_.head).toSet
      )
    )


  private case class SolverState(carry: Int = 0, assignments: Assignments = Map(), usedDigits: UsedDigits = Set())

  private def solveColumns(puzzle: Puzzle, state: SolverState = SolverState()): Option[Assignments] =
    puzzle.columns match
      case Nil =>
        buildSolutionIfPossible(state)
      case column :: remainingColumns =>
        assignAddends(puzzle, column.addends, column.result, remainingColumns, state, sum = 0)

  private def buildSolutionIfPossible(state: SolverState): Option[Assignments] =
    Option.when(state.carry == 0)(state.assignments)

  private def assignAddends(
      puzzle: Puzzle,
      letters: List[Char],
      resultLetter: Char,
      remainingColumns: List[Column],
      state: SolverState,
      sum: Int
  ): Option[Assignments] =
    letters match
      case Nil =>
        assignResult(puzzle, resultLetter, remainingColumns, state, sum)
      case letter :: rest =>
        state.assignments.get(letter) match
          case Some(digit) =>
            assignAddends(puzzle, rest, resultLetter, remainingColumns, state, sum + digit)
          case None =>
            availableDigits(letter, puzzle, state.usedDigits).iterator
              .flatMap { digit =>
                assignAddends(puzzle, rest, resultLetter, remainingColumns, state.copy(
                    assignments = state.assignments + (letter -> digit),
                    usedDigits = state.usedDigits + digit
                  ),
                  sum + digit
                )
              }
              .nextOption()

  private def assignResult(
      puzzle: Puzzle,
      letter: Char,
      remainingColumns: List[Column],
      state: SolverState,
      sum: Int
  ): Option[Assignments] =
    val total = sum + state.carry
    val digit = total % 10
    val nextCarry = total / 10

    state.assignments.get(letter) match
      case Some(assignedDigit) if assignedDigit == digit =>
        solveColumns(puzzle.copy(columns = remainingColumns), state.copy(carry = nextCarry))
      case Some(_) =>
        None
      case None if state.usedDigits.contains(digit) || isLeadingZero(letter, digit, puzzle) =>
        None
      case None =>
        solveColumns(
          puzzle.copy(columns = remainingColumns),
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
