export class GradeSchool {
  #grades = new Map();
  #names = new Set();

  roster() {
    return this.#grades
      .keys()
      .toArray()
      .sort((fst, snd) => fst - snd)
      .flatMap((gr) => this.grade(gr));
  }

  add(name, grade) {
    if (this.#names.has(name)) {
      return false;
    }

    this.#addStudentTo(grade, name);
    this.#names.add(name);
    return true;
  }

  grade(grade) {
    return [...this.#studentsFor(grade)].sort();
  }

  #studentsFor(grade) {
    return this.#grades.get(grade) ?? [];
  }

  #addStudentTo(grade, name) {
    const students = this.#studentsFor(grade);
    students.push(name);
    this.#grades.set(grade, students);
  }
}
