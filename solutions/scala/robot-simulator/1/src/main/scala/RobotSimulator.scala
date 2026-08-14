enum Bearing {
  case North, East, South, West
}

case class Robot(bearing: Bearing, coordinates: (Int, Int)) {
  import Bearing.*

  def turnRight: Robot =
    turn(1)

  def turnLeft: Robot =
    turn(-1)

  def advance: Robot =
    val (x, y) = coordinates

    bearing match {
      case North => copy(coordinates = (x, y + 1))
      case East => copy(coordinates = (x + 1, y))
      case South => copy(coordinates = (x, y - 1))
      case West => copy(coordinates = (x - 1, y))
    }

  def simulate(instructions: String): Robot =
    instructions.foldLeft(this) {
      case (robot, 'R') => robot.turnRight
      case (robot, 'L') => robot.turnLeft
      case (robot, 'A') => robot.advance
      case (robot, _) => robot
    }

  private def turn(steps: Int): Robot =
    val bearings = Bearing.values
    val index = (bearing.ordinal + steps + bearings.length) % bearings.length

    copy(bearing = bearings(index))
}
