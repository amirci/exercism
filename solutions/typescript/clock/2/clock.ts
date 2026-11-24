type Minutes = number

const MINUTES_PER_DAY: Minutes = 24 * 60

const normalizeMinutes = (minutes: Minutes): Minutes =>
  ((minutes % MINUTES_PER_DAY) + MINUTES_PER_DAY) % MINUTES_PER_DAY

export class Clock {
  private readonly minutesSinceMidnight: Minutes

  constructor(hour: number, minute: Minutes = 0) {
    this.minutesSinceMidnight = normalizeMinutes(hour * 60 + minute)
  }

  public toString(): string {
    const hours = Math.floor(this.minutesSinceMidnight / 60)
    const minutes = this.minutesSinceMidnight % 60

    return `${hours.toString().padStart(2, "0")}:${minutes.toString().padStart(2, "0")}`
  }

  public plus(minutes: Minutes): Clock {
    return new Clock(0, this.minutesSinceMidnight + minutes)
  }

  public minus(minutes: Minutes): Clock {
    return new Clock(0, this.minutesSinceMidnight - minutes)
  }

  public equals(other: unknown): boolean {
    return other instanceof Clock && this.minutesSinceMidnight === other.minutesSinceMidnight
  }
}
