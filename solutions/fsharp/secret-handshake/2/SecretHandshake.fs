module SecretHandshake

let private actions =
    [
        1, "wink"
        2, "double blink"
        4, "close your eyes"
        8, "jump"
    ]

let private reverseAction = 16

let private isSet number mask = number &&& mask <> 0

let private actionIfSet number (mask, action) =
    if isSet number mask then Some action else None

let commands number =
    let selected = actions |> List.choose (actionIfSet number)

    if isSet number reverseAction then List.rev selected else selected
