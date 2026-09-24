export class InvalidInputError extends Error {}

type Direction = 'north' | 'east' | 'south' | 'west'
type Coordinates = [number, number]

export class Robot {
  private direction: Direction = 'north'
  private position: Coordinates = [0, 0]

  get bearing(): Direction {
    return this.direction
  }

  get coordinates(): Coordinates {
    return [...this.position] as Coordinates
  }

  place({ x, y, direction }: { x: number; y: number; direction: string }) {
    if (!isDirection(direction)) {
      throw new InvalidInputError('Invalid direction')
    }

    this.position = [x, y]
    this.direction = direction
  }

  evaluate(instructions: string) {
    for (const instruction of instructions) {
      switch (instruction) {
        case 'L':
          this.direction = turnLeft(this.direction)
          break
        case 'R':
          this.direction = turnRight(this.direction)
          break
        case 'A':
          this.position = advance(this.direction, this.position)
          break
        default:
          throw new InvalidInputError('Invalid instruction')
      }
    }
  }
}

const directions: Direction[] = ['north', 'east', 'south', 'west']

const isDirection = (value: string): value is Direction =>
  directions.includes(value as Direction)

const turn = (direction: Direction, amount: number): Direction => {
  const index = directions.indexOf(direction)
  return directions[(index + amount + directions.length) % directions.length]
}

const turnLeft = (direction: Direction) => turn(direction, -1)
const turnRight = (direction: Direction) => turn(direction, 1)

const advance = (direction: Direction, [x, y]: Coordinates): Coordinates => {
  switch (direction) {
    case 'north': return [x, y + 1]
    case 'east': return [x + 1, y]
    case 'south': return [x, y - 1]
    case 'west': return [x - 1, y]
  }
}
