object SpiralMatrix:
  private val Directions = Vector((0, 1), (1, 0), (0, -1), (-1, 0))

  def spiralMatrix(size: Int): List[List[Int]] =
    if size == 0 then List.empty
    else
      val matrix = Array.fill(size, size)(0)
      var row = 0
      var column = 0
      var directionIndex = 0

      for number <- 1 to size * size do
        matrix(row)(column) = number

        val (rowDelta, columnDelta) = Directions(directionIndex)
        val nextRow = row + rowDelta
        val nextColumn = column + columnDelta

        if isOutside(nextRow, nextColumn, size) || matrix(nextRow)(nextColumn) != 0 then
          directionIndex = (directionIndex + 1) % Directions.size

        val (newRowDelta, newColumnDelta) = Directions(directionIndex)
        row += newRowDelta
        column += newColumnDelta

      matrix.map(_.toList).toList

  private def isOutside(row: Int, column: Int, size: Int): Boolean =
    row < 0 || row >= size || column < 0 || column >= size
