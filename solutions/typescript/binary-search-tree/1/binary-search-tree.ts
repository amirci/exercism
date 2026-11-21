export class BinarySearchTree {
  private readonly value: number
  private leftSubtree: BinarySearchTree | undefined
  private rightSubtree: BinarySearchTree | undefined

  constructor(data: number) {
    this.value = data
  }

  public get data(): number {
    return this.value
  }

  public get left(): BinarySearchTree | undefined {
    return this.leftSubtree
  }

  public get right(): BinarySearchTree | undefined {
    return this.rightSubtree
  }

  public insert(item: number): void {
    if (item <= this.value) {
      if (this.leftSubtree === undefined) this.leftSubtree = new BinarySearchTree(item)
      else this.leftSubtree.insert(item)
      return
    }

    if (this.rightSubtree === undefined) this.rightSubtree = new BinarySearchTree(item)
    else this.rightSubtree.insert(item)
  }

  public each(callback: (data: number) => void): void {
    this.leftSubtree?.each(callback)
    callback(this.value)
    this.rightSubtree?.each(callback)
  }
}
