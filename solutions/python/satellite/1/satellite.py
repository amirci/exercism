"""Reconstruct a binary tree from preorder and inorder traversals."""


def _validate_traversals(preorder, inorder):
    """Validate traversal lengths, elements, and uniqueness."""
    if len(preorder) != len(inorder):
        raise ValueError("traversals must have the same length")
    if set(preorder) != set(inorder):
        raise ValueError("traversals must have the same elements")
    if len(set(preorder)) != len(preorder):
        raise ValueError("traversals must contain unique items")


def tree_from_traversals(preorder, inorder):
    """Reconstruct a binary tree from preorder and inorder traversals."""
    _validate_traversals(preorder, inorder)
    inorder_positions = {value: index for index, value in enumerate(inorder)}
    preorder_values = iter(preorder)

    def build(start, end):
        if start == end:
            return {}
        root = next(preorder_values)
        root_index = inorder_positions[root]
        return {
            "v": root,
            "l": build(start, root_index),
            "r": build(root_index + 1, end),
        }

    return build(0, len(inorder))
