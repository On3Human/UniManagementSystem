package views.student;
import models.academic.Exam;
import models.academic.questions.*;
import javax.swing.*;
import java.awt.*;

public class ViewExamDetailsFrame extends JFrame {
    public ViewExamDetailsFrame(Exam e) {
        setTitle("Corrected Exam: " + e.getSubjectName()); setSize(500, 600); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel m = new JPanel(); m.setLayout(new BoxLayout(m, BoxLayout.Y_AXIS));
        
        int i = 1;
        for(Question q : e.getQuestions()) {
            JPanel p = new JPanel(new GridLayout(0, 1));
            p.setBorder(BorderFactory.createTitledBorder("Q" + (i++) + " (" + q.getScore() + " pts)"));
            p.add(new JLabel("Q: " + q.getQuestionText()));
            
            String ca = "Unknown";
            if(q instanceof MultiChoiceQuestion) ca = ((MultiChoiceQuestion)q).getCorrectAnswer();
            else if(q instanceof TrueFalseQuestion) ca = String.valueOf(((TrueFalseQuestion)q).getCorrectAnswer()); // Fixed boolean error
            else if(q instanceof ShortAnswerQuestion) ca = ((ShortAnswerQuestion)q).getCorrectAnswer();
            
            JLabel l = new JLabel("Correct Answer: " + ca); 
            l.setForeground(new Color(0, 100, 0));
            p.add(l); m.add(p);
        }
        add(new JScrollPane(m));
    }
}
