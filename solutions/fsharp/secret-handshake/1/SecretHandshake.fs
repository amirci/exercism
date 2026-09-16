module SecretHandshake

let private actions =
    [
        1, "wink"
        2, "double blink"
        4, "close your eyes"
        8, "jump"
    ]

let commands number =
    let selected =
        actions
        |> List.choose (fun (mask, action) ->
            if number &&& mask <> 0 then Some action else None)

    if number &&& 16 <> 0 then List.rev selected else selected
