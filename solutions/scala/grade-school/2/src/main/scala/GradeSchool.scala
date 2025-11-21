import scala.collection.MapView
import scala.collection.immutable.ListMap

class School {
  private type DB = Map[Int, Seq[String]]

  var db: DB = Map[Int, List[String]]()

  def add(name: String, grade: Int): Unit = {
    val names = db getOrElse(grade, List[String]())
    db = db + (grade -> (names :+ name))
  }

  def grade(gr: Int): Seq[String] = {
    db getOrElse(gr, List[String]())
  }

  def sorted: Map[Int, Seq[String]] = {
    db.toSeq.map(item => (item._1, item._2.sorted)).sortBy(_._1).toMap
  }
}

