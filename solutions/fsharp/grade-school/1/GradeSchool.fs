module GradeSchool

type School = Map<int, string list>

let empty: School = Map.empty

let add (student: string) (grade: int) (school: School): School =
    let alreadyEnrolled =
        school |> Map.exists (fun _ students -> List.contains student students)

    if alreadyEnrolled then
        school
    else
        let students = school |> Map.tryFind grade |> Option.defaultValue []
        school |> Map.add grade (student :: students |> List.sort)

let roster (school: School): string list =
    school |> Map.toList |> List.collect snd

let grade (number: int) (school: School): string list =
    school |> Map.tryFind number |> Option.defaultValue []
