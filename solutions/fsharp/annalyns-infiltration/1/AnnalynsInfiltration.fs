module AnnalynsInfiltration

let canFastAttack knightIsAwake = not knightIsAwake

let canSpy knightIsAwake archerIsAwake prisonerIsAwake =
    knightIsAwake || archerIsAwake || prisonerIsAwake

let canSignalPrisoner archerIsAwake prisonerIsAwake =
    not archerIsAwake && prisonerIsAwake

let canFreePrisoner knightIsAwake archerIsAwake prisonerIsAwake petDogIsPresent =
    not archerIsAwake && (petDogIsPresent || (not knightIsAwake && prisonerIsAwake))
