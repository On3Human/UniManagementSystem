package models.academic;
import models.academic.questions.Question;
import java.io.Serializable;
import java.util.*;
public class Exam implements Serializable {
    private String examId;
    private String subjectName;
    private String duration;
    private List<Question> questions;
    private boolean isPublished;
    private boolean resultsPublished;
    private List<String> feedback;

    public Exam(String id, String sub, String dur) {
        this.examId = id; this.subjectName = sub; this.duration = dur;
        this.questions = new ArrayList<>();
        this.feedback = new ArrayList<>();
        this.isPublished = false;
        this.resultsPublished = false;
    }
    public void addQuestion(Question q) { questions.add(q); }
    public List<Question> getQuestions() { return questions; }
    public String getSubjectName() { return subjectName; }
    public String getExamId() { return examId; }
    
    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean p) { isPublished = p; }
    
    public boolean areResultsPublished() { return resultsPublished; }
    public void setResultsPublished(boolean p) { resultsPublished = p; }
    
    public void addFeedback(String f) { feedback.add(f); }
    public List<String> getFeedbackList() { return feedback; } // ADDED GETTER
}
