package services.interfaces;

import models.academic.Exam;
import models.users.Lecturer;

public interface ILecturerService {
    void createExam(Lecturer lecturer, Exam exam);
}
