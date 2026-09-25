"""Build a binary tree from parent-child records."""

from dataclasses import dataclass, field


@dataclass(frozen=True)
class Record:
    """Describe a node and its parent by ID."""

    record_id: int
    parent_id: int


@dataclass
class Node:
    """Represent a tree node and its children."""

    node_id: int
    children: list["Node"] = field(default_factory=list)


def _validate_records(unordered_records):
    """Validate record IDs and parent relationships."""
    records = sorted(unordered_records, key=lambda record: record.record_id)

    record_ids = [record.record_id for record in records]

    if record_ids != list(range(len(records))):
        raise ValueError("Record id is invalid or out of order.")


    for record in records:
        if record.record_id == record.parent_id:
            if record.record_id != 0:
                raise ValueError("Only root should have equal record and parent id.")
        elif record.parent_id >= record.record_id:
            raise ValueError("Node parent_id should be smaller than its record_id.")

    return records


def BuildTree(records):  # pylint: disable=invalid-name
    """Build and return a tree from records, or None for no records."""
    if not records:
        return None

    ordered_records = _validate_records(records)

    nodes = {record.record_id: Node(record.record_id) for record in ordered_records}

    for record in ordered_records[1:]:
        nodes[record.parent_id].children.append(nodes[record.record_id])

    return nodes[0]
