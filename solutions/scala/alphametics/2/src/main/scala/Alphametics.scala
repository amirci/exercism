object Alphametics {
  private type Digit = Int
  private type Assignments = Map[Char, Digit]
  private type UsedDigits = Set[Digit]

  def solve(input: String): Option[Assignments] =
    parsePuzzle(input).flatMap(puzzle => solveColumns(puzzle))

  private case class Puzzle(columns: List[Column], letters: Set[Char], leadingLetters: Set[Char])

  private case class Column(addends: List[Char], result: Char)

  private def parsePuzzle(input: String): Option[Puzzle] =
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
      case Nil if state.carry != 0 => None
      case Nil => Some(state.assignments)
      case column :: remaining => solveColumn(puzzle, column, remaining, state)

  private def solveColumn(puzzle: Puzzle, column: Column, remaining: List[Column], state: SolverState): Option[Assignments] =
    assignAddendDigits(puzzle, column, remaining, state, sum = 0)

  private def assignAddendDigits(puzzle: Puzzle, current: Column, remaining: List[Column], state: SolverState, sum: Int): Option[Assignments] =
    current.addends match
      case Nil =>
        assignResultDigit(puzzle, current.result, remaining, state, sum)
      case letter :: rest =>
        assignAddendDigit(puzzle, letter, current.copy(addends = rest), remaining, state, sum)

  private def assignAddendDigit(puzzle: Puzzle, letter: Char, current: Column, remaining: List[Column], state: SolverState, sum: Int): Option[Assignments] =
    state.assignments.get(letter) match
      case Some(digit) =>
        assignAddendDigits(puzzle, current, remaining, state, sum + digit)
      case None =>
        availableDigits(letter, puzzle, state.usedDigits).iterator
          .flatMap { digit =>
            assignAddendDigits(
              puzzle,
              current,
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
        solveColumns(puzzle.copy(columns = remaining), state.copy(carry = nextCarry))
      case Some(_) =>
        None
      case None if state.usedDigits.contains(digit) || isLeadingZero(letter, digit, puzzle) =>
        None
      case None =>
        solveColumns(
          puzzle.copy(columns = remaining),
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
