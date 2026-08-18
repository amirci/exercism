object PascalsTriangle {
  def rows(count: Int): Seq[Seq[Int]] =
    if count <= 0 then Vector.empty
    else
      Vector.iterate(Vector(1), count)(nextRow)

  private def nextRow(row: Vector[Int]): Vector[Int] =
    ((0 +: row) zip (row :+ 0)).map { case (left, right) => left + right }
}
