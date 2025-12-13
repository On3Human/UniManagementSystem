package models.academic.questions;

public class MultiChoiceQuestion extends Question {
    private String correctAnswer; 
    private String[] options;

    public MultiChoiceQuestion(String text, double score, String correct, String[] options) {
        super(text, score);
        this.correctAnswer = correct;
        this.options = options;
    }

    @Override
    public boolean checkAnswer(String studentAnswer) {
        return correctAnswer.equalsIgnoreCase(studentAnswer.trim());
    }
    
    public String[] getOptions() { return options; }
}
