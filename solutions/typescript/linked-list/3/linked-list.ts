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

const createList = <E>(
  element: E,
): NonEmptyList<E> => {
  const node = new ListNode(element);
  return { kind: "non-empty", head: node, tail: node, size: 1 };
};

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

type InsertFn<E> = (state: NonEmptyList<E>) => NonEmptyList<E>;

const insertAtTheEnd = <E>(element: E): InsertFn<E> => (state: NonEmptyList<E>)=>{
  const newNode =new ListNode<E>(element);
  newNode.previous = state.tail;
  state.tail.next = newNode;
  state.tail = newNode;
  state.size++;

  return state;
};

const insertAtTheBeginning = <E>(element: E): InsertFn<E> => (state: NonEmptyList<E>)=>{
  const newNode =new ListNode<E>(element);
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
    this.state = this.matchState(insertAtTheEnd(element), () => createList(element));
  }

  public pop(): E | undefined {
    return this.matchState((state) =>
      this.updateAfterDropping(state.tail)(state),
    );
  }

  public shift(): E | undefined {
    return this.matchState((state) =>
      this.updateAfterDropping(state.head)(state),
    );
  }

  public unshift(element: E): void {
    this.state = this.matchState(insertAtTheBeginning(element), () => createList(element));
  }

  public delete(element: E): void {
    this.matchState(
      (state) => {
        const node = findNode(state, element);
        if (node) this.state = removeNode(state, node).state;
      },
    );
  }

  public count(): number {
    return this.matchState((state) => state.size, () => 0);
  }

  private updateAfterDropping(node: ListNode<E>) {
    return (target: NonEmptyList<E>) => {
      const { state, element } = removeNode(target, node);
      this.state = state;
      return element;
    };
  }

  private matchState<Result>(onNonEmpty: (state: NonEmptyList<E>) => Result, onEmpty: () => Result): Result;
  private matchState<Result>(onNonEmpty: (state: NonEmptyList<E>) => Result): Result | undefined;
  private matchState<Result>(
    onNonEmpty: (state: NonEmptyList<E>) => Result,
    onEmpty: () => Result | undefined = () => undefined,
  ): Result | undefined {
    const state = this.state;
    return isEmptyList(state) ? onEmpty() : onNonEmpty(state);
  }
}
