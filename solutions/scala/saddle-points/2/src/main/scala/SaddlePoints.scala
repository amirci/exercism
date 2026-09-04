case class Matrix(matrix: List[List[Int]]):
  def saddlePoints: Set[(Int, Int)] =
    if matrix.isEmpty || matrix.head.isEmpty then Set.empty
    else
      val values = matrix.map(_.toVector).toVector
      val rowMaximums = values.map(_.max)
      val columnMinimums =
        values.head.indices.map(column => values.map(_(column)).min)

      (for
        row <- values.indices
        column <- values(row).indices
        if values(row)(column) == rowMaximums(row)
        if values(row)(column) == columnMinimums(column)
      yield (row, column)).toSet
