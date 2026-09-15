module ListOps

let rec foldl folderFn state list =
    match list with
    | [] -> state
    | head :: tail -> foldl folderFn (folderFn state head) tail

let rec foldr folderFn state list =
    match list with
    | [] -> state
    | head :: tail -> folderFn head (foldr folderFn state tail)

let length list =
    foldl (fun acc _ -> acc + 1) 0 list

let reverse list =
    foldl (fun acc head -> head :: acc) [] list

let map f list =
    foldr (fun head acc -> f head :: acc) [] list

let filter predicate list =
    foldr (fun head acc -> if predicate head then head :: acc else acc) [] list

let append xs ys = foldr (fun head acc -> head :: acc) ys xs

let concat lists =
    foldr append [] lists
