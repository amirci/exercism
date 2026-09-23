module ImprovedPasswordChecker

open System

[<Flags>]
type PasswordError =
    | LessThan12Characters = 1
    | MissingUppercaseLetter = 2
    | MissingLowercaseLetter = 4
    | MissingDigit = 8
    | MissingSymbol = 16

let private symbols = set "!@#$%^&*"

let private validationErrors (password: string): PasswordError list =
    let isMissing predicate = Seq.exists predicate password |> not
    let isMissingSymbol = isMissing (fun character -> Set.contains character symbols)

    [
        if password.Length < 12 then yield PasswordError.LessThan12Characters
        if isMissing Char.IsUpper then yield PasswordError.MissingUppercaseLetter
        if isMissing Char.IsLower then yield PasswordError.MissingLowercaseLetter
        if isMissing Char.IsDigit then yield PasswordError.MissingDigit
        if isMissingSymbol then
            yield PasswordError.MissingSymbol
    ]

/// Validate the given password against the rules defined in the instructions. If it meets all
/// of the rules, return a result indicating success; otherwise return a result indicating
/// failure with an error value indicating all of the rules that were violated.
let checkPassword (password: string) : Result<string, PasswordError> =
    let errors = validationErrors password
    let flags = errors |> List.fold (|||) (enum<PasswordError> 0)

    if flags = enum<PasswordError> 0 then Ok password else Error flags

let private statusPhrases (errors: PasswordError) =
    [
        PasswordError.LessThan12Characters, "12 characters"
        PasswordError.MissingUppercaseLetter, "uppercase letter"
        PasswordError.MissingLowercaseLetter, "lowercase letter"
        PasswordError.MissingDigit, "digit"
        PasswordError.MissingSymbol, "symbol"
    ]
    |> List.choose (fun (flag, phrase) ->
        if errors.HasFlag flag then Some phrase else None)

/// Return a list of human-readable phrases indicating the meaning of the given result value.
let getStatusPhrases (result: Result<string, PasswordError>) : string list =
    match result with
    | Ok _ -> []
    | Error errors -> statusPhrases errors
