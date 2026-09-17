module LuciansLusciousLasagna

let expectedMinutesInOven = 40

let remainingMinutesInOven elapsedMinutes =
    expectedMinutesInOven - elapsedMinutes

let preparationTimeInMinutes layers =
    layers * 2

let elapsedTimeInMinutes layers elapsedMinutes =
    preparationTimeInMinutes layers + elapsedMinutes
