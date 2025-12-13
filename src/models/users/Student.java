package models.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private List<String> enrolledSubjects;
    
    // Key: Exam ID, Value: Score
    private Map<String, Double> examGrades;
    
    // Key: Exam ID, Value: Reason
    private Map<String, String> reCorrectionRequests;

    public Student(String id, String username, String password) {
        super(id, username, password, UserRole.STUDENT);
        this.enrolledSubjects = new ArrayList<>();
        this.examGrades = new HashMap<>();
        this.reCorrectionRequests = new HashMap<>();
    }

    public List<String> getEnrolledSubjects() { return enrolledSubjects; }

    public void enrollSubject(String subject) {
        if (!enrolledSubjects.contains(subject)) {
            enrolledSubjects.add(subject);
        }
    }

    public Map<String, Double> getExamGrades() { return examGrades; }
    public Map<String, String> getReCorrectionRequests() { return reCorrectionRequests; }

    public boolean hasTakenExam(String examId) {
        return examGrades.containsKey(examId);
    }
    
    public void recordGrade(String examId, double score) {
        this.examGrades.put(examId, score);
    }
    
    public void addReCorrectionRequest(String examId, String reason) {
        this.reCorrectionRequests.put(examId, reason);
    }
}
