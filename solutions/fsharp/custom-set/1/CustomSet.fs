module CustomSet

type Set<'a when 'a: equality> =
    private
    | Set of 'a list

let empty = Set []

let singleton value = Set [value]

let isEmpty (Set values) = List.isEmpty values

let size (Set values) = List.length values

let fromList values = Set (List.distinct values)

let toList (Set values) = values

let contains value (Set values) = List.contains value values

let insert value set =
    if contains value set then set else Set (value :: toList set)

let union left right =
    fromList (toList left @ toList right)

let intersection left right =
    toList left
    |> List.filter (fun value -> contains value right)
    |> fromList

let difference left right =
    toList left
    |> List.filter (fun value -> not (contains value right))
    |> fromList

let isSubsetOf left right =
    toList left |> List.forall (fun value -> contains value right)

let isDisjointFrom left right =
    toList left |> List.forall (fun value -> not (contains value right))

let isEqualTo left right =
    isSubsetOf left right && isSubsetOf right left
