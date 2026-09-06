"""Manage an ordered collection with a doubly linked list."""

from collections.abc import Iterator
from dataclasses import dataclass
from typing import Optional


@dataclass(eq=False)
class Node:
    """Store a value and links to its neighboring nodes."""

    value: object
    succeeding: "Node | None" = None
    previous: "Node | None" = None


class LinkedList:
    """Support insertion and removal at both ends of a list."""

    def __init__(self) -> None:
        self.head: Optional[Node] = None
        self.tail: Node | None = None
        self.length = 0

    def __len__(self) -> int:
        return self.length

    def __iter__(self) -> Iterator[Node]:
        current = self.head
        while current is not None:
            yield current
            current = current.succeeding

    def push(self, value: object) -> None:
        """Add a value at the end of the list."""
        node = Node(value, previous=self.tail)
        if self.tail is None:
            self.head = node
        else:
            self.tail.succeeding = node
        self.tail = node
        self.length += 1

    def unshift(self, value: object) -> None:
        """Add a value at the beginning of the list."""
        node = Node(value, succeeding=self.head)
        if self.head is None:
            self.tail = node
        else:
            self.head.previous = node
        self.head = node
        self.length += 1

    def _remove(self, node: Node) -> object:
        """Unlink a node and return its value."""
        if node.previous is None:
            self.head = node.succeeding
        else:
            node.previous.succeeding = node.succeeding

        if node.succeeding is None:
            self.tail = node.previous
        else:
            node.succeeding.previous = node.previous

        node.previous = None
        node.succeeding = None
        self.length -= 1
        return node.value

    def pop(self) -> object:
        """Remove and return the last value."""
        if self.tail is None:
            raise IndexError("List is empty")
        return self._remove(self.tail)

    def shift(self) -> object:
        """Remove and return the first value."""
        if self.head is None:
            raise IndexError("List is empty")
        return self._remove(self.head)

    def delete(self, value: object) -> None:
        """Remove the first occurrence of a value."""
        for node in self:
            if node.value == value:
                self._remove(node)
                return
        raise ValueError("Value not found")
