module RolePlayingGame

type Mana = int
type Damage = int

type Player = {
    Name: string option
    Level: int
    Health: int
    Mana: Mana option
}

type SpellResult = Player * Damage

let introduce: Player -> string = _.Name >> Option.defaultValue "Mighty Magician"

let private fullManaIfAtLeastLevelTen player =
    let mana = if player.Level >= 10 then Some 100 else None
    { player with Mana = mana }

let private fullHealth player = { player with Health = 100 }

let private isDead = _.Health >> ((=) 0)

let revive =
    Some
    >> Option.filter isDead
    >> Option.map (fullHealth >> fullManaIfAtLeastLevelTen)

let private reduceMana cost mana player: Player =
    { player with Mana = Some (mana - cost) }

let private reduceHealth cost player: Player =
    { player with Health = max 0 (player.Health - cost) }

let castSpell (cost: Mana) (player: Player): SpellResult =
    match player.Mana with
    | Some mana when mana >= cost -> (reduceMana cost mana player, cost * 2)
    | Some _ -> player, 0
    | None -> (reduceHealth cost player, 0)
