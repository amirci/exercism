export class Bowling {
  private rolls: number[] = []

  public roll(pins: number): void {
    if (pins < 0) {
      throw new Error("Negative roll is invalid")
    }
    if (pins > 10) {
      throw new Error("Pin count exceeds pins on the lane")
    }
    if (this.isGameOver()) {
      throw new Error("Cannot roll after game is over")
    }

    const frame = this.currentFrame()
    if (frame.frameNumber < 10 && frame.rolls.length === 1 && frame.rolls[0] + pins > 10) {
      throw new Error("Pin count exceeds pins on the lane")
    }
    if (frame.frameNumber === 10 && frame.rolls.length === 1 &&
        frame.rolls[0] !== 10 && frame.rolls[0] + pins > 10) {
      throw new Error("Pin count exceeds pins on the lane")
    }
    if (frame.frameNumber === 10 && frame.rolls.length === 2 &&
        frame.rolls[0] === 10 && frame.rolls[1] !== 10 &&
        frame.rolls[1] + pins > 10) {
      throw new Error("Pin count exceeds pins on the lane")
    }

    this.rolls.push(pins)
  }

  public score(): number {
    if (!this.isGameOver()) {
      throw new Error("Score cannot be taken until the end of the game")
    }

    let total = 0
    let index = 0
    for (let frame = 0; frame < 10; frame += 1) {
      if (this.rolls[index] === 10) {
        total += 10 + this.rolls[index + 1] + this.rolls[index + 2]
        index += 1
      } else if (this.rolls[index] + this.rolls[index + 1] === 10) {
        total += 10 + this.rolls[index + 2]
        index += 2
      } else {
        total += this.rolls[index] + this.rolls[index + 1]
        index += 2
      }
    }
    return total
  }

  private currentFrame(): { frameNumber: number; rolls: number[] } {
    let index = 0
    for (let frameNumber = 1; frameNumber <= 10; frameNumber += 1) {
      if (this.rolls[index] === 10) {
        if (frameNumber === 10) {
          return { frameNumber, rolls: this.rolls.slice(index) }
        }
        index += 1
      } else {
        if (index + 1 >= this.rolls.length) {
          return { frameNumber, rolls: this.rolls.slice(index) }
        }
        index += 2
      }
    }
    return { frameNumber: 10, rolls: this.rolls.slice(index) }
  }

  private isGameOver(): boolean {
    let index = 0
    for (let frame = 0; frame < 9; frame += 1) {
      if (index >= this.rolls.length) return false
      index += this.rolls[index] === 10 ? 1 : 2
      if (index > this.rolls.length) return false
    }

    if (index >= this.rolls.length) return false
    if (this.rolls[index] === 10) return index + 2 < this.rolls.length
    if (index + 1 >= this.rolls.length) return false
    if (this.rolls[index] + this.rolls[index + 1] === 10) {
      return index + 2 < this.rolls.length
    }
    return true
  }
}
