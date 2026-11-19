class ListNode<TElement> {
  public next: ListNode<TElement> | undefined;
  public previous: ListNode<TElement> | undefined;

  public constructor(public readonly element: TElement) {}
}

type MaybeNode<TElement> = ListNode<TElement> | undefined;


export class LinkedList<TElement> {
  private head: MaybeNode<TElement>;
  private tail: MaybeNode<TElement>;
  private size = 0;

  public push(element: TElement): void {
    const node = new ListNode(element);
    node.previous = this.tail;

    if (this.tail) {
      this.tail.next = node;
    } else {
      this.head = node;
    }

    this.tail = node;
    this.size++;
  }

  public pop(): TElement | undefined {
    if (!this.tail) return undefined;
    return this.removeNode(this.tail);
  }

  public shift(): TElement | undefined {
    if (!this.head) return undefined;
    return this.removeNode(this.head);
  }

  public unshift(element: TElement): void {
    const node = new ListNode(element);
    node.next = this.head;

    if (this.head) {
      this.head.previous = node;
    } else {
      this.tail = node;
    }

    this.head = node;
    this.size++;
  }

  public delete(element: TElement): void {
    let node = this.head;

    while (node && node.element !== element) {
      node = node.next;
    }

    if (node) this.removeNode(node);
  }

  public count(): number {
    return this.size;
  }

  private removeNode(node: ListNode<TElement>): TElement {
    if (node.previous) {
      node.previous.next = node.next;
    } else {
      this.head = node.next;
    }

    if (node.next) {
      node.next.previous = node.previous;
    } else {
      this.tail = node.previous;
    }

    this.size--;
    return node.element;
  }
}
