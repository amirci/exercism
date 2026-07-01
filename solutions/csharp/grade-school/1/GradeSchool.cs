using StudentsRoster = System.Collections.Generic.SortedSet<string>;

public class GradeSchool
{
    private readonly SortedDictionary<int, StudentsRoster> _studentsByGrade = [];
    private readonly StudentsRoster _roster = new();

    public bool Add(string student, int grade)
    {
        if (!_roster.Add(student))
        {
            return false;
        }

        UpdateGrade(grade, student);
        return true;
    }

    public IEnumerable<string> Roster() => _studentsByGrade.Values
        .SelectMany(students => students);

    public IEnumerable<string> Grade(int grade) => StudentsForGrade(grade);

    private StudentsRoster StudentsForGrade(int grade) =>
        _studentsByGrade.TryGetValue(grade, out var students) ? students : EmptyRoster();

    private void UpdateGrade(int grade, string student)
    {
        var students = StudentsForGrade(grade);
        students.Add(student);
        _studentsByGrade[grade] = students;
    }

    private static StudentsRoster EmptyRoster() => [];
}
