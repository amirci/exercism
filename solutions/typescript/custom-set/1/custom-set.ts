type Predicate<E> = (element: E) => boolean;

export class CustomSet<T = unknown> {
  private readonly values: Set<T>

  constructor(initial: Iterable<T> = []) {
    this.values = new Set(initial)
  }

  empty(): boolean {
    return this.values.size === 0
  }

  contains(element: T): boolean {
    return this.values.has(element)
  }

  private hasElement(element: unknown): boolean {
    return this.values.has(element as T)
  }

  add(element: T): this {
    this.values.add(element)
    return this
  }

  private isIncludedIn(other: CustomSet<T>): Predicate<T> {
    return (element: T) => other.hasElement(element)
  }

  private isNotIncludedIn(other: CustomSet<T>): Predicate<T> {
    return (element: T) => !other.hasElement(element)
  }

  subset(other: CustomSet<T>): boolean {
      return [...this.values].every(this.isIncludedIn(other))
  }

  disjoint(other: CustomSet<T>): boolean {
      return [...this.values].every(this.isNotIncludedIn(other))
  }

  eql(other: CustomSet<T>): boolean {
    return this.values.size === other.values.size && this.subset(other)
  }

  union(other: CustomSet<T>): CustomSet<T> {
    return new CustomSet<T>([...this.values, ...other.values])
  }

  private newSetWith(predicate: Predicate<T>): CustomSet<T> {
    return new CustomSet([...this.values].filter(predicate))
  }

  intersection(other: CustomSet<T>): CustomSet<T> {
    return this.newSetWith(this.isIncludedIn(other))
  }

  difference(other: CustomSet<T>): CustomSet<T> {
    return this.newSetWith(this.isNotIncludedIn(other))
  }

}
