package models.academic.questions;
public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;
    public TrueFalseQuestion(String t, double s, boolean c) { super(t, s); correctAnswer=c; }
    @Override public boolean checkAnswer(String a) { return Boolean.parseBoolean(a) == correctAnswer; }
    public boolean getCorrectAnswer() { return correctAnswer; }
}
