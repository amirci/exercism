object Wordy {
  def answer(question: String): Option[Int] =
    val expression = question
      .stripPrefix("What is ")
      .stripSuffix("?")
      .replace("multiplied by", "multiplied")
      .replace("divided by", "divided")
      .split(" ")
      .toList

    expression match {
      case first :: rest => first.toIntOption.flatMap(evaluate(_, rest))
      case _ => None
    }

  private def evaluate(current: Int, tokens: List[String]): Option[Int] =
    tokens match {
      case Nil => Some(current)
      case operator :: value :: rest =>
        for
          number <- value.toIntOption
          result <- applyOperator(current, operator, number)
          answer <- evaluate(result, rest)
        yield answer
      case _ => None
    }

  private def applyOperator(left: Int, operator: String, right: Int): Option[Int] =
    operator match {
      case "plus" => Some(left + right)
      case "minus" => Some(left - right)
      case "multiplied" => Some(left * right)
      case "divided" => Some(left / right)
      case _ => None
    }
}
