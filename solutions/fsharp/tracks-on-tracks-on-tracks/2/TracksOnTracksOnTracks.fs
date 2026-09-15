module TracksOnTracksOnTracks

let newList: string list = []

let existingList: string list = [ "F#"; "Clojure"; "Haskell" ]

let addLanguage language languages = language :: languages

let countLanguages = List.length

let reverseList = List.rev

let excitingList (languages: string list): bool =
    match languages with
    | "F#" :: _
    | [ _; "F#" ]
    | [ _; "F#"; _ ] -> true
    | _ -> false
