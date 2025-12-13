package services.impl;

import exception.ValidationException;
import models.academic.Exam;
import models.academic.questions.Question;
import models.users.Student;
import services.interfaces.IStudentService;
import storage.DataManager;

import java.util.Map;

public class StudentServiceImpl implements IStudentService {
    private DataManager dataManager = DataManager.getInstance();

    @Override
    public double takeExam(Student student, Exam exam, Map<Integer, String> answers) throws ValidationException {
        // 1. Validation: Is Student enrolled in this Subject?
        if (!student.getEnrolledSubjects().contains(exam.getSubjectName())) {
            throw new ValidationException("You are not enrolled in " + exam.getSubjectName());
        }

        // 2. Validation: Has Student already taken this exam?
        if (student.hasTakenExam(exam.getExamId())) {
            throw new ValidationException("You have already taken this exam.");
        }
        
        // 3. Validation: Is the exam published?
        // Note: Usually students shouldn't see unpublished exams, but this is a safety check.
        if (!exam.isPublished()) {
            throw new ValidationException("This exam is not yet published.");
        }

        // 4. Automatic Grading Logic
        double totalScore = 0;
        int questionIndex = 0;
        
        for (Question q : exam.getQuestions()) {
            // answers map uses Index (0, 1, 2) as key to match question order
            String studentAnswer = answers.get(questionIndex);
            
            if (studentAnswer != null && q.checkAnswer(studentAnswer)) {
                totalScore += q.getScore();
            }
            questionIndex++;
        }

        // 5. Save Result
        student.recordGrade(exam.getExamId(), totalScore);
        dataManager.saveData();

        return totalScore;
    }

    @Override
    public void requestReCorrection(Student student, String examId, String reason) throws ValidationException {
        if (!student.hasTakenExam(examId)) {
            throw new ValidationException("Cannot request re-correction for an exam you haven't taken.");
        }
        
        student.addReCorrectionRequest(examId, reason);
        dataManager.saveData();
    }

    @Override
    public void submitFeedback(Student student, Exam exam, String feedback) {
        if (feedback != null && !feedback.isEmpty()) {
            exam.addFeedback(student.getUsername() + ": " + feedback);
            dataManager.saveData();
        }
    }
}
