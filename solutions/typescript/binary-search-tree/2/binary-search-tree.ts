type Subtree = BinarySearchTree | undefined

export class BinarySearchTree {
  private readonly value: number
  private leftSubtree: Subtree
  private rightSubtree: Subtree

  constructor(data: number) {
    this.value = data
  }

  public get data(): number {
    return this.value
  }

  public get left(): Subtree {
    return this.leftSubtree
  }

  public get right(): Subtree {
    return this.rightSubtree
  }

  public insert(item: number): this {
    if (item <= this.value) {
      this.leftSubtree = this.insertInto(this.leftSubtree, item)
    } else {
      this.rightSubtree = this.insertInto(this.rightSubtree, item)
    }

    return this
  }

  private insertInto(subtree: Subtree, item: number): BinarySearchTree {
    return subtree?.insert(item) ?? new BinarySearchTree(item)
  }

  public each(callback: (data: number) => void): void {
    this.leftSubtree?.each(callback)
    callback(this.value)
    this.rightSubtree?.each(callback)
  }
}
