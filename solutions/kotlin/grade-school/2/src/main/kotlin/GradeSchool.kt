private typealias Student = String
private typealias Grade = Int

class School {
    private val studentsByGrade = mutableMapOf<Grade, List<Student>>()

    fun add(student: Student, grade: Grade) {
        require(student !in roster())

        studentsByGrade[grade] = studentsIn(grade) + student
    }

    fun grade(grade: Grade): List<Student> = studentsIn(grade).sorted()

    fun roster(): List<Student> = studentsByGrade
        .toSortedMap()
        .values
        .flatMap { it.sorted() }

    private fun studentsIn(grade: Grade): List<Student> = studentsByGrade.getOrDefault(grade, emptyList())
}
