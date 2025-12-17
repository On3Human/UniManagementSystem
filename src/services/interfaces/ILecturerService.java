package services.interfaces;
import models.academic.Exam;
import models.users.Lecturer;
public interface ILecturerService {
    void createExam(Lecturer l, Exam e);
    void deleteExam(String id); // Req: Manage
}
