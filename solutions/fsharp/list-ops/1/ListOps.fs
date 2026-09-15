module ListOps

let rec foldl folder state list =
    match list with
    | [] -> state
    | head :: tail -> foldl folder (folder state head) tail

let rec foldr folder state list =
    match list with
    | [] -> state
    | head :: tail -> folder (foldr folder state tail) head

let length list =
    foldl (fun count _ -> count + 1) 0 list

let reverse list =
    foldl (fun reversed head -> head :: reversed) [] list

let map f list =
    foldr (fun mapped head -> f head :: mapped) [] list

let filter predicate list =
    foldr (fun filtered head -> if predicate head then head :: filtered else filtered) [] list

let append xs ys =
    foldr (fun appended head -> head :: appended) ys xs

let concat lists =
    foldr (fun concatenated current -> append current concatenated) [] lists
