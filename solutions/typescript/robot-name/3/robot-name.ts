const LETTERS = [..."ABCDEFGHIJKLMNOPQRSTUVWXYZ"]
const DIGITS = [..."0123456789"]

const chooseElements = <T>(items: readonly T[], count: number): T[] =>
  Array.from({ length: count }, () => items[Math.floor(Math.random() * items.length)])

const parts: [readonly string[], number][] = [
    [LETTERS, 2],
    [DIGITS, 3],
]

export class Robot {
  private static readonly usedNames = new Set<string>()
  private roboName: string

  constructor() {
    this.roboName = Robot.generateAndRegisterUniqueName()
  }

  public get name(): string {
    return this.roboName
  }

  public resetName(): void {
    this.roboName = Robot.generateAndRegisterUniqueName()
  }

  public static releaseNames(): void {
    Robot.usedNames.clear()
  }

  private static generateAndRegisterUniqueName(): string {
    let name: string
    do {
      name = Robot.randomName()
    } while (Robot.usedNames.has(name))

    Robot.usedNames.add(name)
    return name
  }

  private static randomName(): string {
    return parts
      .map(([source, count]) => chooseElements(source, count))
      .map((part) => part.join(""))
      .join("")
  }
}
