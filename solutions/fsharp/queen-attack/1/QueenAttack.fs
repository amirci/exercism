module QueenAttack

let private onBoard (row, column) =
    row >= 0 && row < 8 && column >= 0 && column < 8

let create position = onBoard position

let canAttack (row1, column1) (row2, column2) =
    row1 = row2
    || column1 = column2
    || abs (row1 - row2) = abs (column1 - column2)
