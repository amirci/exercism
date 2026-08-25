class School {
    private val studentsByGrade = mutableMapOf<Int, List<String>>()

    fun add(student: String, grade: Int) {
        require(student !in roster())

        studentsByGrade[grade] = studentsIn(grade) + student
    }

    fun grade(grade: Int): List<String> = studentsIn(grade).sorted()

    fun roster(): List<String> = studentsByGrade
        .toSortedMap()
        .values
        .flatMap { students -> students.sorted() }

    private fun studentsIn(grade: Int): List<String> = studentsByGrade.getOrDefault(grade, emptyList())
}
