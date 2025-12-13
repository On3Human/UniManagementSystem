package views.student;

import exception.ValidationException;
import models.academic.Exam;
import models.academic.questions.Question;
import models.users.Student;
import services.impl.StudentServiceImpl;
import services.interfaces.IStudentService;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TakeExamFrame extends JFrame {
    private Student student;
    private Exam exam;
    private StudentDashboard parent;
    private Map<Integer, JTextField> answerFields;

    public TakeExamFrame(Student student, Exam exam, StudentDashboard parent) {
        this.student = student;
        this.exam = exam;
        this.parent = parent;
        this.answerFields = new HashMap<>();

        setTitle("Exam: " + exam.getSubjectName());
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        int index = 0;
        for (Question q : exam.getQuestions()) {
            JPanel qPanel = new JPanel(new GridLayout(2, 1));
            qPanel.setBorder(BorderFactory.createTitledBorder("Q" + (index + 1) + " (" + q.getScore() + " pts)"));
            qPanel.add(new JLabel(q.getQuestionText()));
            JTextField ansField = new JTextField();
            qPanel.add(ansField);
            mainPanel.add(qPanel);
            answerFields.put(index, ansField);
            index++;
        }

        JButton submitBtn = new JButton("Submit Exam");
        submitBtn.addActionListener(e -> submitExam());

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(submitBtn, BorderLayout.SOUTH);
    }

    private void submitExam() {
        Map<Integer, String> answers = new HashMap<>();
        for (Map.Entry<Integer, JTextField> entry : answerFields.entrySet()) {
            answers.put(entry.getKey(), entry.getValue().getText());
        }

        IStudentService service = new StudentServiceImpl();
        try {
            double score = service.takeExam(student, exam, answers);
            JOptionPane.showMessageDialog(this, "Submitted! Score: " + score);
            parent.refresh();
            this.dispose();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
