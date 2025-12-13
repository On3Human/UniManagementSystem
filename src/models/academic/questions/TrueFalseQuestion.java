package models.academic.questions;

public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String text, double score, boolean correct) {
        super(text, score);
        this.correctAnswer = correct;
    }

    @Override
    public boolean checkAnswer(String studentAnswer) {
        // Expecting "true" or "false" string input
        return Boolean.parseBoolean(studentAnswer) == correctAnswer;
    }
}
