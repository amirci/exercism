class School {
  private type DB = Map[Int, Seq[String]]

  var db: DB = Map().withDefaultValue(Seq())

  def add(name: String, grade: Int): Unit = {
    val names = db(grade)
    db += grade -> (names :+ name)
  }

  def grade(gr: Int): Seq[String] = db(gr)

  def sorted: Map[Int, Seq[String]] = {
    db.toSeq.map(item => (item._1, item._2.sorted)).sortBy(_._1).toMap
  }
}

