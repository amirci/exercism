object MatchingBrackets {
  def isPaired(brackets: String): Boolean = {
    val opens = Set('(', '[', '{')
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

    brackets.foldLeft(List.empty[Char]) { (stack, char) =>
      if (!opens(char) && !pairs.contains(char)) stack
      else (opens(char), stack.headOption, pairs.get(char)) match {
        case (true, _, _) => char :: stack
        case (false, Some(_), None) => return false
        case (false, Some(opening), Some(closing)) if opening == closing  => stack.tail
        case (false, Some(opening), Some(closing)) if opening != closing  => return false
        case _ => return false
      }
    }.isEmpty
  }
}
