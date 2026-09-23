module LogLevels

let private separatorIndex (logLine: string) = logLine.IndexOf "]:"

let message (logLine: string): string =
    let separator = separatorIndex logLine
    logLine.Substring(separator + 2).Trim()

let logLevel (logLine: string): string =
    let separator = separatorIndex logLine
    logLine.Substring(1, separator - 1).ToLowerInvariant()

let reformat (logLine: string): string =
    sprintf "%s (%s)" (message logLine) (logLevel logLine)
