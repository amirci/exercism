export class GradeSchool {
  #grades = new Map();

  roster() {
    return Array
      .from(this.#grades.keys())
      .sort((fst, snd) => fst - snd)
      .flatMap((gr) => this.grade(gr));
  }

  add(name, grade) {
    if (this.roster().includes(name)) {
      return false;
    }

    this.#addStudentTo(grade, name);
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
