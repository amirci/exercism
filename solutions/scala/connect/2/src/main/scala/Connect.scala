enum Color {
  case Black, White
}

case class Connect(board: List[String]) {
  import Color.*

  private case class Position(row: Int, column: Int)
  private case class SearchConfig(marker: Char, starts: List[Position], isTarget: Position => Boolean)

  private val NeighborOffsets = List((-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0))
  private val cells: Map[Position, Char] =
    board.zipWithIndex.flatMap { case (row, rowIndex) =>
      row.zipWithIndex.map { case (marker, columnIndex) =>
        Position(rowIndex, columnIndex) -> marker
      }
    }.toMap

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
          starts = cells.keys.filter(_.column == 0).toList,
          isTarget = position => position.column == lastColumnIn(position.row)
        )
      case White =>
        SearchConfig(
          marker = 'O',
          starts = cells.keys.filter(_.row == 0).toList,
          isTarget = position => position.row == board.length - 1
        )

  private def neighbors(position: Position): List[Position] =
    NeighborOffsets
      .map { case (rowOffset, columnOffset) => Position(position.row + rowOffset, position.column + columnOffset) }

  private def lastColumnIn(row: Int): Int =
    board(row).length - 1

  private def hasMarker(position: Position, marker: Char): Boolean =
    cells.get(position).contains(marker)
}
