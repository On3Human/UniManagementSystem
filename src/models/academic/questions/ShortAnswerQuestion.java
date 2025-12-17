package models.academic.questions;
public class ShortAnswerQuestion extends Question {
    private String correctAnswer;
    public ShortAnswerQuestion(String t, double s, String c) { super(t, s); correctAnswer=c; }
    @Override public boolean checkAnswer(String a) { return a!=null && correctAnswer.equalsIgnoreCase(a.trim()); }
    public String getCorrectAnswer() { return correctAnswer; }
}
