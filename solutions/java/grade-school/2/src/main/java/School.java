import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class School {
    private final Map<Integer, Set<String>> studentsByGrade = new TreeMap<>();
    private final Set<String> students = new HashSet<>();

    boolean add(String student, int grade) {
        return !isEnrolled(student) && enrollStudent(student, grade);
    }

    private boolean isEnrolled(String student) {
        return students.contains(student);
    }

    private boolean enrollStudent(String student, int grade) {
        students.add(student);

        return studentsByGrade
                .computeIfAbsent(grade, ignored -> new TreeSet<>())
                .add(student);
    }

    List<String> roster() {
        return studentsByGrade.values().stream()
                .flatMap(Set::stream)
                .toList();
    }

    List<String> grade(int grade) {
        return studentsByGrade
                .getOrDefault(grade, Set.of())
                .stream()
                .toList();
    }

}
