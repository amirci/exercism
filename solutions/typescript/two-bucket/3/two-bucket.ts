type BucketName = "one" | "two"
type BucketState = readonly [bucketOne: number, bucketTwo: number]

type SearchState = {
  state: BucketState
  moves: number
}

type Queue<T> = {
  entries: T[]
  head: number
}

const createQueue = <T>(): Queue<T> => ({ entries: [], head: 0 })

const enqueue = <T>(queue: Queue<T>, entry: T): void => {
  queue.entries.push(entry)
}

const emptyQueue = <T>(queue: Queue<T>): boolean =>
  queue.head >= queue.entries.length

const dequeue = <T>(queue: Queue<T>): T => {
  if (emptyQueue(queue)) throw new Error("Cannot dequeue from an empty queue")
  return queue.entries[queue.head++]
}

type Measurement = {
  moves: number
  goalBucket: BucketName
  otherBucket: number
}

type MeasurementResult =
  | { kind: "success"; value: Measurement }
  | { kind: "failure"; error: unknown }

export class TwoBucket {
  private readonly result: MeasurementResult

  constructor(
    private readonly bucketOneSize: number,
    private readonly bucketTwoSize: number,
    private readonly goal: number,
    private readonly startBucket: BucketName
  ) {
    try {
      this.result = { kind: "success", value: this.measure() }
    } catch (error) {
      this.result = { kind: "failure", error }
    }
  }

  moves(): number {
    return this.measurement().moves
  }

  get goalBucket(): BucketName {
    return this.measurement().goalBucket
  }

  get otherBucket(): number {
    return this.measurement().otherBucket
  }

  private measurement(): Measurement {
    if (this.result.kind === "failure") throw this.result.error
    return this.result.value
  }

  private measure(): Measurement {
    this.validateInputs()

    const initialState = this.initialBucketState()
    const queue = createQueue<SearchState>()
    enqueue(queue, { state: initialState, moves: 1 })
    const visited = new Set<string>([this.stateKey(initialState)])

    while (!emptyQueue(queue)) {
      const { state, moves } = dequeue(queue)
      const result = this.resultFor(state, moves)
      if (result !== undefined) {
        return result
      }

      for (const nextState of this.unvisitedValidStates(state, visited)) {
        visited.add(this.stateKey(nextState))
        enqueue(queue, { state: nextState, moves: moves + 1 })
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

  private unvisitedValidStates(state: BucketState, visited: Set<string>): BucketState[] {
    return this.nextStates(state).filter(nextState =>
      !this.isForbidden(nextState) && !visited.has(this.stateKey(nextState))
    )
  }

  private initialBucketState(): BucketState {
    return this.startBucket === "one"
      ? [this.bucketOneSize, 0]
      : [0, this.bucketTwoSize]
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
