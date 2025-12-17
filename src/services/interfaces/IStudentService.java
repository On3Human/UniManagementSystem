package services.interfaces;
import exception.ValidationException;
import models.academic.Exam;
import models.users.Student;
import java.util.Map;
public interface IStudentService {
    double takeExam(Student s, Exam e, Map<Integer, String> ans) throws ValidationException;
    void requestReCorrection(Student s, String eid, String r) throws ValidationException;
    void submitFeedback(Student s, Exam e, String f);
}
