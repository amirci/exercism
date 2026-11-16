export class GradeSchool {
  #grades = new Map();

  roster() {
    return Array.from(this.#grades.keys())
      .sort((first, second) => first - second)
      .flatMap((grade) => this.grade(grade));
  }

  add(name, grade) {
    if (this.roster().includes(name)) {
      return false;
    }

    const students = this.#grades.get(grade) ?? [];
    students.push(name);
    this.#grades.set(grade, students);
    return true;
  }

  grade(grade) {
    return [...(this.#grades.get(grade) ?? [])].sort();
  }
}
