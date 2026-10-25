const LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const DIGITS = "0123456789"

export class Robot {
  private static readonly usedNames = new Set<string>()
  private roboName: string

  constructor() {
    this.roboName = Robot.generateName()
  }

  public get name(): string {
    return this.roboName
  }

  public resetName(): void {
    this.roboName = Robot.generateName()
  }

  public static releaseNames(): void {
    Robot.usedNames.clear()
  }

  private static generateName(): string {
    let name: string
    do {
      const letters = Array.from({ length: 2 }, () => LETTERS[Math.floor(Math.random() * LETTERS.length)])
      const digits = Array.from({ length: 3 }, () => DIGITS[Math.floor(Math.random() * DIGITS.length)])
      name = letters.join("") + digits.join("")
    } while (Robot.usedNames.has(name))

    Robot.usedNames.add(name)
    return name
  }
}
