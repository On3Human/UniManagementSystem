package services.impl;
import exception.ValidationException;
import models.academic.Exam;
import models.academic.questions.Question;
import models.users.Student;
import services.interfaces.IStudentService;
import storage.DataManager;
import java.util.Map;

public class StudentServiceImpl implements IStudentService {
    private DataManager db = DataManager.getInstance();
    
    public double takeExam(Student s, Exam e, Map<Integer, String> ans) throws ValidationException {
        // Req 3.4.a: Access only if registered
        boolean enrolled = false;
        for(String sub : s.getEnrolledSubjects()) {
            if(sub.trim().equalsIgnoreCase(e.getSubjectName().trim())) { enrolled = true; break; }
        }
        if(!enrolled) throw new ValidationException("You are not enrolled in Subject: " + e.getSubjectName());
        
        // Req 3.4.b: One-time Entry
        if(s.hasTakenExam(e.getExamId())) throw new ValidationException("You have already taken this exam.");
        
        // Req 1.2.d: Access only if Published
        if(!e.isPublished()) throw new ValidationException("This exam is not published yet.");

        // Req Lecturer.b: Automatic Grading
        double score = 0;
        int idx = 0;
        for(Question q : e.getQuestions()) {
            String studentAns = ans.get(idx++);
            if(studentAns != null && q.checkAnswer(studentAns)) {
                score += q.getScore();
            }
        }
        s.recordGrade(e.getExamId(), score);
        db.saveData();
        return score;
    }
    
    public void requestReCorrection(Student s, String eid, String r) throws ValidationException {
        s.addReCorrectionRequest(eid, r); db.saveData();
    }
    
    public void submitFeedback(Student s, Exam e, String f) {
        e.addFeedback(s.getUsername() + ": " + f); db.saveData();
    }
}
