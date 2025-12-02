import java.util.Scanner;

public abstract class Question {

    protected String questionText;
    protected double questionWeight;
    protected String correctAnswer;

    public Question(String questionText, double questionWeight, String correctAnswer) {
        this.questionText = questionText;
        this.questionWeight = questionWeight;
        this.correctAnswer = correctAnswer;
    }

    // 4. Abstract method: Subclasses MUST define how to calculate the score
    // (MCQ might check "A", ShortAnswer might check text)
    public abstract double calculateScore(String studentAnswer);

    public void displayQuestion() {
        System.out.println(questionText);
    }

    public double promptAndGrade(Scanner sc) {
        displayQuestion();
        System.out.print("Your Answer: ");
        String studentResponse = sc.nextLine();
        return calculateScore(studentResponse);
    }
}