package models.academic;
import models.academic.questions.Question;
import java.io.Serializable;
import java.util.*;
public class Exam implements Serializable {
    private String examId;
    private String subjectName;
    private String duration; // Req: Specify Duration
    private List<Question> questions;
    private boolean isPublished; // Req: Grade Approval/Publication
    private List<String> feedback; // Req: Feedback

    public Exam(String id, String sub, String dur) {
        this.examId = id; this.subjectName = sub; this.duration = dur;
        this.questions = new ArrayList<>();
        this.feedback = new ArrayList<>();
        this.isPublished = false;
    }
    public void addQuestion(Question q) { questions.add(q); }
    public List<Question> getQuestions() { return questions; }
    public String getSubjectName() { return subjectName; }
    public String getExamId() { return examId; }
    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean p) { isPublished = p; }
    public void addFeedback(String f) { feedback.add(f); }
}
