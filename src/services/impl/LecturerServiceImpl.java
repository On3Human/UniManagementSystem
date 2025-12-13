package services.impl;

import models.academic.Exam;
import models.users.Lecturer;
import services.interfaces.ILecturerService;
import storage.DataManager;

public class LecturerServiceImpl implements ILecturerService {
    private DataManager dataManager = DataManager.getInstance();

    @Override
    public void createExam(Lecturer lecturer, Exam exam) {
        // In the future, you might throw ValidationException here if exam has no questions
        lecturer.addExam(exam);
        dataManager.getAllExams().add(exam);
        dataManager.saveData();
        // REMOVED: System.out.println("Exam created..."); 
        // The GUI will assume success if no exception is thrown.
    }
}
