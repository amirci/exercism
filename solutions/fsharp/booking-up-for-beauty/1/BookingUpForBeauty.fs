module BookingUpForBeauty

open System

let schedule appointmentDateDescription =
    DateTime.Parse appointmentDateDescription

let hasPassed (appointmentDate: DateTime) =
    appointmentDate < DateTime.Now

let isAfternoonAppointment (appointmentDate: DateTime) =
    appointmentDate.Hour >= 12 && appointmentDate.Hour < 18

let description (appointmentDate: DateTime) =
    sprintf "You have an appointment on %O." appointmentDate

let anniversaryDate () =
    DateTime(DateTime.Now.Year, 9, 15)
