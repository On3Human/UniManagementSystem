package models.academic.questions;
import java.io.Serializable;
public abstract class Question implements Serializable {
    protected String questionText;
    protected double score;
    public Question(String t, double s) { this.questionText = t; this.score = s; }
    public abstract boolean checkAnswer(String a);
    public String getQuestionText() { return questionText; }
    public double getScore() { return score; }
}
