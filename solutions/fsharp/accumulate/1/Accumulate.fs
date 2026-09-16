module Accumulate

let accumulate (func: 'a -> 'b) (input: 'a list): 'b list =
    let rec loop remaining accumulated =
        match remaining with
        | [] -> List.rev accumulated
        | head :: tail -> loop tail (func head :: accumulated)

    loop input []
