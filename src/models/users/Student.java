package models.users;

public class Student extends User {
    public Student(String id, String username, String password) {
        super(id, username, password, UserRole.STUDENT);
    }
    // Future: Add logic to hold taken exams and grades
}
