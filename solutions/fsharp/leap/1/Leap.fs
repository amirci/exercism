module Leap

let leapYear (year: int): bool =
    let isMultiple divisor = year % divisor = 0
    isMultiple 400 || (isMultiple 4 && not (isMultiple 100))
