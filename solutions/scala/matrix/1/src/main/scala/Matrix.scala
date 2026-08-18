case class Matrix(input: String) {
  private val rows: Vector[Vector[Int]] =
    input.linesIterator
      .map(_.split(" ").map(_.toInt).toVector)
      .toVector

  def row(index: Int): Vector[Int] =
    rows(index)

  def column(index: Int): Vector[Int] =
    rows.map(_(index))
}
