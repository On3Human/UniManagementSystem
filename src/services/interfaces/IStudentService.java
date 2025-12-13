package services.interfaces;

import exception.ValidationException;
import models.academic.Exam;
import models.users.Student;
import java.util.Map;

public interface IStudentService {
    // a) Access & b) One-time Entry & Grading
    // Returns the final score
    double takeExam(Student student, Exam exam, Map<Integer, String> answers) throws ValidationException;

    // d) Re-correction
    void requestReCorrection(Student student, String examId, String reason) throws ValidationException;

    // e) Feedback
    void submitFeedback(Student student, Exam exam, String feedback);
}
