
class Triangle(val a: Double, val b: Double, val c: Double) {
  def equilateral: Boolean = is_valid && sides.distinct.size == 1

  def isosceles: Boolean = is_valid && Set(1, 2)(sides.distinct.size)

  def scalene: Boolean = is_valid && sides.distinct.size == 3

  private def sides = List(a, b, c).sorted
  private def is_valid = sides.head + sides(1) > sides(2) && sides.forall(_ > 0)
}
