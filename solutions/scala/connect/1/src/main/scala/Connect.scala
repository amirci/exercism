enum Color {
  case Black, White
}

case class Connect(board: List[String]) {
  import Color.*

  private case class Position(row: Int, column: Int)
  private case class SearchConfig(
      marker: Char,
      starts: List[Position],
      isTarget: Position => Boolean
  )
  private val NeighborOffsets = List((-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0))

  def winner: Option[Color] =
    List(Black, White).find(hasPath)

  private def hasPath(color: Color): Boolean =
    val config = configFor(color)
    val starts = config.starts.filter(hasMarker(_, config.marker))

    starts.exists(start => reachesTarget(config, start))

  private def reachesTarget(config: SearchConfig, start: Position): Boolean =
    def search(visited: Set[Position], pending: List[Position]): Boolean =
      pending match
        case Nil => false
        case position :: rest if config.isTarget(position) => true
        case position :: rest =>
          val nextPositions = neighbors(position).filter(hasMarker(_, config.marker)).filterNot(visited)
          search(visited ++ nextPositions, nextPositions ++ rest)

    search(Set(start), List(start))

  private def configFor(color: Color): SearchConfig =
    color match
      case Black =>
        SearchConfig(
          marker = 'X',
          starts = board.indices.map(row => Position(row, 0)).toList,
          isTarget = position => position.column == board(position.row).length - 1
        )
      case White =>
        SearchConfig(
          marker = 'O',
          starts = board.headOption.toList.flatMap(row => row.indices.map(column => Position(0, column))),
          isTarget = position => position.row == board.length - 1
        )

  private def neighbors(position: Position): List[Position] =
    NeighborOffsets
      .map { case (rowOffset, columnOffset) => Position(position.row + rowOffset, position.column + columnOffset) }
      .filter(isInside)

  private def isInside(position: Position): Boolean =
    position.row >= 0 && position.row < board.length && position.column >= 0 && position.column < board(position.row).length

  private def hasMarker(position: Position, marker: Char): Boolean =
    board(position.row)(position.column) == marker
}
