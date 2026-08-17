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

  override val skipWhitespace = false

  def parseSgf(text: String): Option[SgfTree] =
    parseAll(tree, text) match
      case Success(tree, _) => Some(tree)
      case _ => None

  private def tree: Parser[SgfTree] =
    "(" ~> rep1(node) ~ rep(tree) <~ ")" ^^ {
      case nodes ~ children => buildTree(nodes, children)
    }

  private def node: Parser[SgfNode] =
    ";" ~> rep(property) ^^ (_.toMap)

  private def property: Parser[(String, List[String])] =
    propertyName ~ rep1(value) ^^ {
      case name ~ values => name -> values
    }

  private def propertyName: Parser[String] =
    "[A-Z]+".r

  private def value: Parser[String] =
    "[" ~> rep(valuePart) <~ "]" ^^ (_.mkString)

  private def valuePart: Parser[String] =
    escapedPart | normalPart

  private def escapedPart: Parser[String] =
    "\\" ~> ("\r\n" ^^^ "" | "\n" ^^^ "" | "\r" ^^^ "" | ".".r ^^ (text => normalize(text.head).toString))

  private def normalPart: Parser[String] =
    """[^\]\\]""".r ^^ (text => normalize(text.head).toString)

  private def normalize(character: Char): Char =
    if character.isWhitespace then ' ' else character

  private def buildTree(nodes: List[SgfNode], children: Forest[SgfNode]): SgfTree =
    nodes.reverse match
      case last :: previous =>
        previous.foldLeft(Node(last, children))((child, node) => Node(node, List(child)))
      case Nil =>
        throw new IllegalArgumentException("Trees must contain at least one node")
}
