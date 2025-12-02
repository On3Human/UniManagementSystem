public class MCQQuestion extends Question {
    private String[] options;

    public MCQQuestion(String text, double weight, String ans, String[] options) {
        super(text, weight, ans);
        this.options = options;
    }

    @Override
    public void displayQuestion() {
        super.displayQuestion(); // Print the question text
        for (String opt : options) {
            System.out.println(opt);
        }
    }

    @Override
    public double calculateScore(String studentAnswer) {
        // We assume the correct answer is the letter (e.g., "A")
        // .trim() removes accidental spaces
        if (studentAnswer.trim().equalsIgnoreCase(correctAnswer)) {
            return questionWeight;
        }
        return 0.0;
    }
}