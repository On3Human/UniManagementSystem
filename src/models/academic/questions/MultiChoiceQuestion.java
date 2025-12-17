package models.academic.questions;
public class MultiChoiceQuestion extends Question {
    private String correctAnswer; 
    private String[] options;
    public MultiChoiceQuestion(String t, double s, String c, String[] o) { super(t, s); correctAnswer=c; options=o; }
    @Override public boolean checkAnswer(String a) { return correctAnswer.equalsIgnoreCase(a.trim()); }
    public String getCorrectAnswer() { return correctAnswer; }
    public String[] getOptions() { return options; }
}
