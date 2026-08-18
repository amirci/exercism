object PascalsTriangle {
  def rows(count: Int): List[List[Int]] =
    Iterator
      .iterate(List(1))(nextRow)
      .take(count.max(0))
      .toList

  private def nextRow(row: List[Int]): List[Int] =
    (0 :: row).zip(row :+ 0).map(_ + _)
}
