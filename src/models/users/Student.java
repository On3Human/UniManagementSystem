package models.users;
import java.util.*;
public class Student extends User {
    private List<String> enrolledSubjects;
    private Map<String, Double> examGrades; // Key: ExamID, Value: Score
    private Map<String, String> reCorrectionRequests;

    public Student(String id, String u, String p) {
        super(id, u, p, UserRole.STUDENT);
        this.enrolledSubjects = new ArrayList<>();
        this.examGrades = new HashMap<>();
        this.reCorrectionRequests = new HashMap<>();
    }
    public List<String> getEnrolledSubjects() { return enrolledSubjects; }
    public void enrollSubject(String s) { if(!enrolledSubjects.contains(s)) enrolledSubjects.add(s); }
    public Map<String, Double> getExamGrades() { return examGrades; }
    public boolean hasTakenExam(String eid) { return examGrades.containsKey(eid); }
    public void recordGrade(String eid, double score) { examGrades.put(eid, score); }
    public void addReCorrectionRequest(String eid, String r) { reCorrectionRequests.put(eid, r); }
}
