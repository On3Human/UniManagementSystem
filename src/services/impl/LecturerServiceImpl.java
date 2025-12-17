package services.impl;
import models.academic.Exam;
import models.users.Lecturer;
import services.interfaces.ILecturerService;
import storage.DataManager;
public class LecturerServiceImpl implements ILecturerService {
    private DataManager db = DataManager.getInstance();
    public void createExam(Lecturer l, Exam e) {
        l.addExam(e); db.getAllExams().add(e); db.saveData();
    }
    public void deleteExam(String id) {
        db.getAllExams().removeIf(e -> e.getExamId().equals(id));
        db.saveData();
    }
}
