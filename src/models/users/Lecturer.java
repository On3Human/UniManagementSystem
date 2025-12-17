package models.users;
import models.academic.Exam;
import java.util.ArrayList;
import java.util.List;
public class Lecturer extends User {
    private List<Exam> createdExams;
    private List<String> teachingSubjects;
    public Lecturer(String id, String u, String p) {
        super(id, u, p, UserRole.LECTURER);
        this.createdExams = new ArrayList<>();
        this.teachingSubjects = new ArrayList<>();
    }
    public void addExam(Exam e) { createdExams.add(e); }
    public void assignSubject(String s) { if(!teachingSubjects.contains(s)) teachingSubjects.add(s); }
}
