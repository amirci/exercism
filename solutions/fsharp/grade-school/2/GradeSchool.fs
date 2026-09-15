module GradeSchool

type Grade = int

type Student = string

type School = Map<Grade, Student list>

let empty: School = Map.empty

let grade (number: Grade) (school: School): Student list =
    school |> Map.tryFind number |> Option.defaultValue []

let private containsStudentInAnyGrade student school =
    school |> Map.exists (fun _ students -> List.contains student students)

let add (student: Student) (target: Grade) (school: School): School =
    school
    |> containsStudentInAnyGrade student
    |> function
        | true -> school
        | _ ->
          let students = school |> grade target
          school |> Map.add target (student :: students |> List.sort)


let roster (school: School): Student list =
    school |> Map.toList |> List.collect snd
