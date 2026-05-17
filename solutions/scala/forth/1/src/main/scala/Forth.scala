import ForthError.{ForthError, InvalidWord, UnknownWord}

type NumberStack = List[Int]

type UpdateFn[T] = (t: T) => T

type EvalFn = (context: EvalContext) => EvalState

type OpMap = Map[String, EvalFn]

case class EvalContext(numbers: NumberStack, ops: OpMap)

type EvalState = Either[ForthError, EvalContext]

private val emptyStack: NumberStack = Nil

def initialContext = EvalContext(emptyStack, defaultOps)

extension (ctxt: EvalContext)
  def withNumbers(numbers: NumberStack): EvalState =
    Right(ctxt.copy(numbers = numbers))

def updateNumbers(fn: UpdateFn[NumberStack])(ctxt: EvalContext): EvalState =
  ctxt.withNumbers(fn(ctxt.numbers))

def parseNumber(maybeNum: String, ctxt: EvalContext): Option[EvalState] =
  maybeNum.toIntOption.map(nbr => updateNumbers(nbr :: _)(ctxt))

def opWithTopTwo(fn: (a: Int, b: Int, rest: NumberStack) => NumberStack): EvalFn = { (ctxt: EvalContext) =>
  ctxt.numbers match {
    case a :: b :: rest => ctxt.withNumbers(fn(a, b, rest))
    case _ => Left(ForthError.StackUnderflow)
  }
}

def opWithTop(fn: (a: Int, rest: NumberStack) => NumberStack): EvalFn = { (ctxt: EvalContext) =>
  ctxt.numbers match {
    case a :: rest => ctxt.withNumbers(fn(a, rest))
    case _ => Left(ForthError.StackUnderflow)
  }
}

def binaryOp(op: (Int, Int) => Int): EvalFn = opWithTopTwo { (a, b, rest) => op(b, a) :: rest }

private val swapTopTwo = opWithTopTwo { (a, b, rest) => b :: a :: rest }

private val copySecondOverFirst = opWithTopTwo { (a, b, rest) => b :: a :: b :: rest }

def intDivisionWithGuards(ctxt: EvalContext): EvalState = ctxt.numbers match {
  case 0 :: rest => Left(ForthError.DivisionByZero)
  case _ => binaryOp(_ / _)(ctxt)
}

private val duplicateTop = opWithTop { (a, rest) => a :: a :: rest }

private val dropTop = opWithTop { (_, rest) => rest }

def evalRegisteredOp(maybeOp: String, ctxt: EvalContext): Option[EvalState] =
  ctxt.ops
    .get(maybeOp.toLowerCase)
    .map(op => op(ctxt))

extension (iter: IterableOnce[String])
  def runProgram(init: EvalContext): EvalState =
    iter.iterator.foldLeft(Right(init): EvalState) { (state, token) =>
      state.flatMap(parseAndEval(token))
    }

private def isValidDefinitionName(newUserDef: String): Boolean = newUserDef.toIntOption.isEmpty

def addUserDefined(newUserDef: String, program: List[String], ctxt: EvalContext): EvalState = {
  val capturedOps = ctxt.ops
  val runUserDef: EvalFn = innerCtxt =>
    program
      .runProgram(innerCtxt.copy(ops = capturedOps))
      .map(result => result.copy(ops = innerCtxt.ops))

  if isValidDefinitionName(newUserDef)
  then Right(ctxt.copy(ops = ctxt.ops + (newUserDef.toLowerCase -> runUserDef)))
  else Left(InvalidWord)
}

def evalUserDefinition(maybeUserDef: String, ctxt: EvalContext): Option[EvalState] = maybeUserDef
  .replace(";", "")
  .split("""\s+""")
  .toList match {
    case ":" :: opName :: definition => Some(addUserDefined(opName, definition, ctxt))
    case _ => None
  }

private val defaultOps: OpMap = Map(
  "+" -> binaryOp(_ + _),
  "-" -> binaryOp(_ - _),
  "*" -> binaryOp(_ * _),
  "/" -> intDivisionWithGuards,
  "dup" -> duplicateTop,
  "drop" -> dropTop,
  "swap" -> swapTopTwo,
  "over" -> copySecondOverFirst
)

private val evaluators = List(parseNumber, evalRegisteredOp, evalUserDefinition)

def parseAndEval(token: String)(ctxt: EvalContext): EvalState =
  evaluators
    .iterator
    .map(_(token, ctxt))
    .collectFirst { case Some(result) => result }
    .getOrElse(Left(UnknownWord))

case class StackEvaluatorState(numbers: NumberStack) extends ForthEvaluatorState {
  override def toString: String = numbers.reverse.mkString(" ")
}

extension (s: String)
  def toTokens: Iterator[String] = """:.*?;|\S+""".r
    .findAllMatchIn(s)
    .map(_.matched)

class Forth {
  def eval(text: String): Either[ForthError, ForthEvaluatorState] =
    text
      .toTokens
      .runProgram(initialContext)
      .map(context => StackEvaluatorState(context.numbers))

}
