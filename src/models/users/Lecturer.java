package models.users;

import models.academic.Exam;
import java.util.ArrayList;
import java.util.List;

public class Lecturer extends User {
    private List<Exam> createdExams;
    private List<String> teachingSubjects;

    public Lecturer(String id, String username, String password) {
        super(id, username, password, UserRole.LECTURER);
        this.createdExams = new ArrayList<>();
        this.teachingSubjects = new ArrayList<>();
    }

    public void addExam(Exam exam) {
        this.createdExams.add(exam);
    }

    public List<Exam> getCreatedExams() {
        return createdExams;
    }

    public List<String> getTeachingSubjects() {
        return teachingSubjects;
    }

    public void assignSubject(String subject) {
        if (!teachingSubjects.contains(subject)) {
            teachingSubjects.add(subject);
        }
    }
}
