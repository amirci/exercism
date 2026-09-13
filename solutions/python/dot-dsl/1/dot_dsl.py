"""Build graph data structures from a small tuple-based DSL."""

from dataclasses import dataclass
from functools import reduce

NODE, EDGE, ATTR = range(3)


@dataclass
class Node:
    """Represent a graph node and its attributes."""

    name: str
    attrs: dict


@dataclass
class Edge:
    """Represent a directed graph edge and its attributes."""

    src: str
    dst: str
    attrs: dict


def _parse_node(item):
    if item[0] != NODE:
        return None

    if len(item) < 3:
        raise TypeError("Graph item incomplete")

    if len(item) != 3:
        raise ValueError("Node is malformed")

    _, name, attrs = item

    if not isinstance(name, str) or not isinstance(attrs, dict):
        raise ValueError("Node is malformed")

    return Node(name, attrs)


def _parse_edge(item):
    if item[0] != EDGE:
        return None

    if len(item) != 4:
        raise ValueError("Edge is malformed")

    _, src, dst, attrs = item

    valid_endpoints = all(isinstance(value, str) for value in (src, dst))

    if not valid_endpoints or not isinstance(attrs, dict):
        raise ValueError("Edge is malformed")

    return Edge(src, dst, attrs)


def _parse_attr(item):
    if item[0] != ATTR:
        return None

    if len(item) < 3:
        raise TypeError("Graph item incomplete")

    if len(item) != 3:
        raise ValueError("Attribute is malformed")

    _, name, value = item

    if not isinstance(name, str) or not isinstance(value, str):
        raise ValueError("Attribute is malformed")

    return name, value


def _add_item(graph, item):
    if not isinstance(item, (tuple, list)) or not item:
        raise TypeError("Graph item incomplete")

    match _parse_node(item) or _parse_edge(item) or _parse_attr(item):
        case Node() as node:
            graph.nodes.append(node)
        case Edge() as edge:
            graph.edges.append(edge)
        case (name, value):
            graph.attrs[name] = value
        case _:
            raise ValueError("Unknown item")

    return graph


class Graph:  # pylint: disable=too-few-public-methods
    """Collect nodes, edges, and graph attributes from DSL data."""

    def __init__(self, data=None):
        if data is None:
            data = []
        if not isinstance(data, list):
            raise TypeError("Graph data malformed")

        self.nodes = []
        self.edges = []
        self.attrs = {}

        reduce(_add_item, data, self)
