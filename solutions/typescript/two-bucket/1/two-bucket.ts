type BucketName = "one" | "two"
type BucketState = readonly [bucketOne: number, bucketTwo: number]

type SearchState = {
  state: BucketState
  moves: number
}

type Measurement = {
  moves: number
  goalBucket: BucketName
  otherBucket: number
}

export class TwoBucket {
  private result: Measurement | undefined

  constructor(
    private readonly bucketOneSize: number,
    private readonly bucketTwoSize: number,
    private readonly goal: number,
    private readonly startBucket: BucketName
  ) {}

  moves(): number {
    return this.measure().moves
  }

  get goalBucket(): BucketName {
    return this.measure().goalBucket
  }

  get otherBucket(): number {
    return this.measure().otherBucket
  }

  private measure(): Measurement {
    if (this.result !== undefined) return this.result
    this.validateInputs()

    const initialState: BucketState = this.startBucket === "one"
      ? [this.bucketOneSize, 0]
      : [0, this.bucketTwoSize]
    const queue: SearchState[] = [{ state: initialState, moves: 1 }]
    const visited = new Set<string>([this.stateKey(initialState)])

    for (let queueIndex = 0; queueIndex < queue.length; queueIndex++) {
      const { state, moves } = queue[queueIndex]
      const result = this.resultFor(state, moves)
      if (result !== undefined) {
        this.result = result
        return result
      }

      for (const nextState of this.nextStates(state)) {
        if (this.isForbidden(nextState)) continue

        const key = this.stateKey(nextState)
        if (visited.has(key)) continue

        visited.add(key)
        queue.push({ state: nextState, moves: moves + 1 })
      }
    }

    throw new Error("Goal cannot be reached")
  }

  private validateInputs(): void {
    if (this.bucketOneSize <= 0 || this.bucketTwoSize <= 0 || this.goal <= 0) {
      throw new Error("Bucket sizes and goal must be positive")
    }

    if (this.goal > Math.max(this.bucketOneSize, this.bucketTwoSize)) {
      throw new Error("Goal cannot be reached")
    }

    if (this.goal % this.greatestCommonDivisor(this.bucketOneSize, this.bucketTwoSize) !== 0) {
      throw new Error("Goal cannot be reached")
    }
  }

  private nextStates([amountOne, amountTwo]: BucketState): BucketState[] {
    const [oneAfterPouring, twoAfterPouring] = this.pour(
      amountOne,
      amountTwo,
      this.bucketTwoSize
    )
    const [twoAfterPouringBack, oneAfterPouringBack] = this.pour(
      amountTwo,
      amountOne,
      this.bucketOneSize
    )

    return [
      [this.bucketOneSize, amountTwo],
      [amountOne, this.bucketTwoSize],
      [0, amountTwo],
      [amountOne, 0],
      [oneAfterPouring, twoAfterPouring],
      [oneAfterPouringBack, twoAfterPouringBack],
    ]
  }

  private pour(source: number, target: number, targetCapacity: number): [number, number] {
    const amount = Math.min(source, targetCapacity - target)
    return [source - amount, target + amount]
  }

  private isForbidden([amountOne, amountTwo]: BucketState): boolean {
    return this.startBucket === "one"
      ? amountOne === 0 && amountTwo === this.bucketTwoSize
      : amountTwo === 0 && amountOne === this.bucketOneSize
  }

  private resultFor([amountOne, amountTwo]: BucketState, moves: number): Measurement | undefined {
    if (amountOne === this.goal) {
      return { moves, goalBucket: "one", otherBucket: amountTwo }
    }

    if (amountTwo === this.goal) {
      return { moves, goalBucket: "two", otherBucket: amountOne }
    }

    return undefined
  }

  private stateKey([amountOne, amountTwo]: BucketState): string {
    return `${amountOne},${amountTwo}`
  }

  private greatestCommonDivisor(first: number, second: number): number {
    while (second !== 0) {
      const remainder = first % second
      first = second
      second = remainder
    }

    return first
  }
}
