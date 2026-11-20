class ListNode<E> {
  public next: ListNode<E> | undefined;
  public previous: ListNode<E> | undefined;

  public constructor(public readonly element: E) {}
}

const emptyList = { kind: "empty" } as const;

type EmptyList = typeof emptyList;

type NonEmptyList<E> = {
  kind: "non-empty";
  head: ListNode<E>;
  tail: ListNode<E>;
  size: number;
};

type ListState<E> = EmptyList | NonEmptyList<E>;

const createNonEmptyList = <E>(
  node: ListNode<E>,
): NonEmptyList<E> => ({ kind: "non-empty", head: node, tail: node, size: 1 });

const isEmptyList = <E>(
  state: ListState<E>,
): state is EmptyList => state.kind === "empty";

const findNode = <E>(
  state: NonEmptyList<E>,
  element: E,
): ListNode<E> | undefined => {
  let node: ListNode<E> | undefined = state.head;
  while (node && node.element !== element) {
    node = node.next;
  }
  return node;
};

const insertAtTheEnd = <E>(
  state: ListState<E>,
  element: E,
): NonEmptyList<E> => {
  const newNode =new ListNode<E>(element);
  if (isEmptyList(state)) return createNonEmptyList(newNode);

  newNode.previous = state.tail;
  state.tail.next = newNode;
  state.tail = newNode;
  state.size++;

  return state;
};

const insertAtTheBeginning = <E>(
  state: ListState<E>,
  element: E,
): NonEmptyList<E> => {
  const newNode =new ListNode<E>(element);

  if (isEmptyList(state)) return createNonEmptyList(newNode);

  newNode.next = state.head;
  state.head.previous = newNode;
  state.head = newNode;
  state.size++;

  return state;
};

type Removal<E> = {state: ListState<E>; element: E;};

const removeNode = <E>(
  state: NonEmptyList<E>,
  node: ListNode<E>,
): Removal<E> => {
  const { previous, next } = node;

  if (state.size === 1) {
      node.next = node.previous = undefined;
      return { state: emptyList, element: node.element };
  }

  if (previous) {
    previous.next = next;
  } else if (next) {
    state.head = next;
  }

  if (next) {
    next.previous = previous;
  } else if (previous) {
    state.tail = previous;
  }

  node.next = node.previous = undefined;

  state.size--;

  return { state, element: node.element };
};


export class LinkedList<E> {
  private state: ListState<E> = emptyList;

  public push(element: E): void {
    this.state = insertAtTheEnd(this.state, element);
  }

  public pop(): E | undefined {
    const state = this.state;
    if (isEmptyList(state)) return undefined;

    return this.updateAfterDropping(state, state.tail);
  }

  public shift(): E | undefined {
    const state = this.state;
    if (isEmptyList(state)) return undefined;

    return this.updateAfterDropping(state, state.head);
  }

  public unshift(element: E): void {
    this.state = insertAtTheBeginning(this.state, element);
  }

  public delete(element: E): void {
    const state = this.state;
    if (isEmptyList(state)) return;

    const node = findNode(state, element);
    if (!node) return;

    this.state = removeNode(state, node).state;
  }

  public count(): number {
    return isEmptyList(this.state) ? 0 : this.state.size;
  }

  private updateAfterDropping(target: NonEmptyList<E>, node: ListNode<E>) {
    const {state, element} = removeNode(target, node);
    this.state = state;
    return element;
  }
}
