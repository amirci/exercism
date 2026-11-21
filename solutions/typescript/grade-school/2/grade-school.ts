type Grade = number
type StudentName = string
type StudentNames = StudentName[]
type GradeMap = Record<Grade, StudentNames>

function clone<T>(o: T): T {
  if(!o) { return o }
  return JSON.parse(JSON.stringify(o))
}

export class GradeSchool {
  private readonly school: GradeMap = {}
  private students = new Map<StudentName, Grade>()

  private removeExistingStudent = (name: StudentName): void => {
    const grade = this.students.get(name)
    if (!grade) { return }
    this.school[grade] = this.school[grade].filter((n) => n !== name)
    this.students.delete(name)
  }

  readonly roster = (): GradeMap => clone(this.school)

  add = (name: StudentName, grade: Grade): GradeMap => {
    this.removeExistingStudent(name)
    const existing = this.school[grade] ?? []
    existing.push(name)
    this.school[grade] = existing.sort()
    this.students.set(name, grade)
    return this.school
  }

  readonly grade = (gr: Grade): StudentNames => {
    return clone(this.school[gr]) ?? []
  }
}

