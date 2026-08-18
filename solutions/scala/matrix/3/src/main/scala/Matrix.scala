case class Matrix(rows: Vector[Vector[Int]]) {
  def row(index: Int): Vector[Int] =
    rows(index)

  def column(index: Int): Vector[Int] =
    rows.map(_(index))
}

object Matrix {
  def apply(input: String): Matrix =
    Matrix(
      input
        .split("\\r?\\n")
        .map(_.split(" ").map(_.toInt).toVector)
        .toVector
    )
}
