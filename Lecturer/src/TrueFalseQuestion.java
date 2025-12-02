public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(String text, double weight, String ans) {
        super(text, weight, ans);
    }

    @Override
    public double calculateScore(String studentAnswer) {
        if (studentAnswer.equalsIgnoreCase(correctAnswer)) {
            return questionWeight;
        }
        return 0.0;
    }
}