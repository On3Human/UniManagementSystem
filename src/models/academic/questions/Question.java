package models.academic.questions;

import java.io.Serializable;

public abstract class Question implements Serializable {
    protected String questionText;
    protected double score;

    public Question(String questionText, double score) {
        this.questionText = questionText;
        this.score = score;
    }

    public abstract boolean checkAnswer(String studentAnswer);

    public String getQuestionText() { return questionText; }
    public double getScore() { return score; }
}
