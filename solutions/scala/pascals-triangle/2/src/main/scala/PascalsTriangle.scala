object PascalsTriangle {
  def rows(count: Int): List[List[Int]] =
    if count <= 0 then List()
    else
      (1 until count).foldLeft(List(List(1))) { (triangle, _) =>
        triangle :+ nextRow(triangle.last)
      }

  private def nextRow(row: List[Int]): List[Int] =
    (0 :: row).zip(row :+ 0).map(_ + _)
}
