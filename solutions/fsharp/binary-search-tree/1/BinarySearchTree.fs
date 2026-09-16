module BinarySearchTree

type Tree =
    {
        Data: int
        Left: Tree option
        Right: Tree option
    }

let left node = node.Left

let right node = node.Right

let data node = node.Data

let private leaf value =
    Some { Data = value; Left = None; Right = None }

let private insertChild insertFn value branch =
    branch |> Option.map (insertFn value) |> Option.orElse (leaf value)

let rec private insert value node =
    if value <= node.Data then
        { node with Left = insertChild insert value node.Left }
    else
        { node with Right = insertChild insert value node.Right }

let create items =
    match items with
    | [] -> invalidArg "items" "A tree requires at least one value."
    | root :: values ->
        let initial = { Data = root; Left = None; Right = None }
        values |> List.fold (fun tree value -> insert value tree) initial

let sortedData node =
    let rec traverse current result =
        match current with
        | None -> result
        | Some currentNode ->
            let result = traverse currentNode.Right result
            traverse currentNode.Left (currentNode.Data :: result)

    traverse (Some node) []
