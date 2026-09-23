module PasswordChecker

type PasswordError =
    | LessThan12Characters
    | MissingUppercaseLetter
    | MissingLowercaseLetter
    | MissingDigit
    | MissingSymbol

let private symbols = set "!@#$%^&*"

let private validationError (password: string): PasswordError option =
    let isMissing p = Seq.exists p password |> not
    let specialSymbols ch = Set.contains ch symbols

    if password.Length < 12 then Some LessThan12Characters
    elif isMissing System.Char.IsUpper then Some MissingUppercaseLetter
    elif isMissing System.Char.IsLower then Some MissingLowercaseLetter
    elif isMissing System.Char.IsDigit then Some MissingDigit
    elif isMissing specialSymbols then Some MissingSymbol
    else None

/// Validate the given password against the rules defined in the instructions. If it meets all
/// of the rules, return a result indicating success; otherwise return a result indicating
/// failure and an error indicating which rule was violated.
let checkPassword (password: string) : Result<string, PasswordError> =
    match validationError password with
    | Some error -> Error error
    | None -> Ok password

/// Return a human-readable message indicating the meaning of the given result value.
let getStatusMessage (result: Result<string, PasswordError>) : string =
    match result with
    | Ok _ -> "OK"
    | Error LessThan12Characters -> "Error: does not have at least 12 characters"
    | Error MissingUppercaseLetter -> "Error: does not have at least one uppercase letter"
    | Error MissingLowercaseLetter -> "Error: does not have at least one lowercase letter"
    | Error MissingDigit -> "Error: does not have at least one digit"
    | Error MissingSymbol -> "Error: does not have at least one symbol"
