module AllYourBase

let private toDecimal (inputBase: int) (digits: int list) =
    digits
    |> List.fold (fun value digit -> value * bigint inputBase + bigint digit) 0I

let private fromDecimal (outputBase: int) (value: bigint) =
    let rec collect remaining result =
        if remaining = 0I then
            result
        else
            let baseValue = bigint outputBase
            let quotient = remaining / baseValue
            let remainder = remaining % baseValue
            collect quotient (int remainder :: result)

    if value = 0I then [0] else collect value []

let private validBase = (<=) 2

let rebase digits inputBase outputBase =
    let validDigits = digits |> List.forall (fun digit -> digit >= 0 && digit < inputBase)

    if not (validBase inputBase && validBase outputBase && validDigits) then
        None
    else
        digits |> toDecimal inputBase |> fromDecimal outputBase |> Some
