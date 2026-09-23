module LogLevels

open System.Text.RegularExpressions

let private logLinePattern = Regex @"^\[(?<level>[^\]]+)\]:\s*(?<message>.*)$"

let private parseLogLine (logLine: string) =
    let matched = logLinePattern.Match logLine
    let level = matched.Groups["level"].Value.ToLowerInvariant()
    let text = matched.Groups["message"].Value.Trim()
    level, text

let message (logLine: string): string =
    logLine |> parseLogLine |> snd

let logLevel (logLine: string): string =
    logLine |> parseLogLine |> fst

let reformat (logLine: string): string =
    sprintf "%s (%s)" (message logLine) (logLevel logLine)
