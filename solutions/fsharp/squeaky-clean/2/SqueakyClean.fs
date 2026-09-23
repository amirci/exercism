module SqueakyClean

open System

let transform (c: char) : string =
    if c = '-' then
        "_"
    elif Char.IsWhiteSpace c || Char.IsDigit c then
        ""
    elif Char.IsUpper c then
        $"-{Char.ToLowerInvariant c}"
    elif c >= 'α' && c <= 'ω' then
        "?"
    else
        string c

let clean (identifier: string): string =
    String.collect transform identifier
