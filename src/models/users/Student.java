package models.users;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledSubjects;

    public Student(String id, String username, String password) {
        super(id, username, password, UserRole.STUDENT);
        this.enrolledSubjects = new ArrayList<>();
    }

    public List<String> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void enrollSubject(String subject) {
        if (!enrolledSubjects.contains(subject)) {
            enrolledSubjects.add(subject);
        }
    }
}
