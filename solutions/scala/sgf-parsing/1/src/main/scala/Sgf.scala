import scala.util.parsing.combinator.RegexParsers

object Sgf extends RegexParsers {

  type Tree[A] = Node[A] // to separate the type from the constructor, cf. Haskell's Data.Tree
  type Forest[A] = List[Tree[A]]
  case class Node[A](rootLabel: A, subForest: Forest[A] = List())

  // A tree of nodes.
  type SgfTree = Tree[SgfNode]

  // A node is a property list, each key can only occur once.
  // Keys may have multiple values associated with them.
  type SgfNode = Map[String, List[String]]

  def parseSgf(text: String): Option[SgfTree] =
    parseTree(text, 0).collect {
      case (tree, index) if index == text.length => tree
    }

  private def parseTree(text: String, start: Int): Option[(SgfTree, Int)] =
    for
      afterOpen <- consume(text, start, '(')
      (nodes, afterNodes) <- parseNodes(text, afterOpen)
      (children, afterChildren) <- parseChildren(text, afterNodes)
      afterClose <- consume(text, afterChildren, ')')
    yield (buildTree(nodes, children), afterClose)

  private def parseNodes(text: String, start: Int): Option[(List[SgfNode], Int)] =
    parseNode(text, start).map { case (node, index) =>
      val (nodes, nextIndex) = parseMoreNodes(text, index)
      (node :: nodes, nextIndex)
    }

  private def parseMoreNodes(text: String, start: Int): (List[SgfNode], Int) =
    parseNode(text, start) match
      case Some((node, index)) =>
        val (nodes, nextIndex) = parseMoreNodes(text, index)
        (node :: nodes, nextIndex)
      case None =>
        (List(), start)

  private def parseNode(text: String, start: Int): Option[(SgfNode, Int)] =
    consume(text, start, ';').flatMap { index =>
      parseProperties(text, index).map { case (properties, nextIndex) =>
        (properties.toMap, nextIndex)
      }
    }

  private def parseProperties(text: String, start: Int): Option[(List[(String, List[String])], Int)] =
    parseProperty(text, start) match
      case Some((property, index)) =>
        parseProperties(text, index).map { case (properties, nextIndex) =>
          (property :: properties, nextIndex)
        }
      case None =>
        Some((List(), start))

  private def parseProperty(text: String, start: Int): Option[((String, List[String]), Int)] =
    val (name, afterName) = text.drop(start).span(_.isUpper)

    if name.isEmpty then None
    else
      parseValues(text, start + name.length).map { case (values, index) =>
        ((name, values), index)
      }

  private def parseValues(text: String, start: Int): Option[(List[String], Int)] =
    parseValue(text, start).map { case (value, index) =>
      val (values, nextIndex) = parseMoreValues(text, index)
      (value :: values, nextIndex)
    }

  private def parseMoreValues(text: String, start: Int): (List[String], Int) =
    parseValue(text, start) match
      case Some((value, index)) =>
        val (values, nextIndex) = parseMoreValues(text, index)
        (value :: values, nextIndex)
      case None =>
        (List(), start)

  private def parseValue(text: String, start: Int): Option[(String, Int)] =
    consume(text, start, '[').flatMap { index =>
      val value = new StringBuilder
      var current = index
      var closed = false

      while current < text.length && !closed do
        text.charAt(current) match
          case ']' =>
            closed = true
            current += 1
          case '\\' if current + 1 < text.length =>
            current = appendEscaped(text, current + 1, value)
          case character =>
            value.append(normalize(character))
            current += 1

      Option.when(closed)((value.toString, current))
    }

  private def appendEscaped(text: String, index: Int, value: StringBuilder): Int =
    text.charAt(index) match
      case '\r' if index + 1 < text.length && text.charAt(index + 1) == '\n' =>
        index + 2
      case '\n' | '\r' =>
        index + 1
      case character =>
        value.append(normalize(character))
        index + 1

  private def normalize(character: Char): Char =
    if character.isWhitespace then ' ' else character

  private def parseChildren(text: String, start: Int): Option[(Forest[SgfNode], Int)] =
    parseTree(text, start) match
      case Some((tree, index)) =>
        parseChildren(text, index).map { case (children, nextIndex) =>
          (tree :: children, nextIndex)
        }
      case None =>
        Some((List(), start))

  private def buildTree(nodes: List[SgfNode], children: Forest[SgfNode]): SgfTree =
    nodes.reverse match
      case last :: previous =>
        previous.foldLeft(Node(last, children))((child, node) => Node(node, List(child)))
      case Nil =>
        throw new IllegalArgumentException("Trees must contain at least one node")

  private def consume(text: String, index: Int, character: Char): Option[Int] =
    Option.when(index < text.length && text.charAt(index) == character)(index + 1)
}
