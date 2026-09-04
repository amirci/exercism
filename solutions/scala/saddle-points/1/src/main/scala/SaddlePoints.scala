case class Matrix(matrix: List[List[Int]]):
  def saddlePoints: Set[(Int, Int)] =
    if matrix.isEmpty || matrix.head.isEmpty then Set.empty
    else
      val rowMaximums = matrix.map(_.max)
      val columnMinimums = matrix.transpose.map(_.min)

      (for
        row <- matrix.indices
        column <- matrix(row).indices
        if matrix(row)(column) == rowMaximums(row)
        if matrix(row)(column) == columnMinimums(column)
      yield (row, column)).toSet
