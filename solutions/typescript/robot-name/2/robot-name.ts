const LETTERS = [..."ABCDEFGHIJKLMNOPQRSTUVWXYZ"]
const DIGITS = [..."0123456789"]

const chooseElements = <T>(items: readonly T[], count: number): T[] =>
  Array.from({ length: count }, () => items[Math.floor(Math.random() * items.length)])

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
    return chooseElements(LETTERS, 2).join("") + chooseElements(DIGITS, 3).join("")
  }
}
