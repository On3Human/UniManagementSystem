package models.academic;

import models.academic.questions.Question;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exam implements Serializable {
    private String examId;
    private String subjectName;
    private List<Question> questions;

    public Exam(String examId, String subjectName) {
        this.examId = examId;
        this.subjectName = subjectName;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getSubjectName() { return subjectName; }
    public String getExamId() { return examId; }
}
