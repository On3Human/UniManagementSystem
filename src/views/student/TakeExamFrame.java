package views.student;
import models.academic.Exam;
import models.academic.questions.Question;
import models.users.Student;
import services.impl.StudentServiceImpl;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TakeExamFrame extends JFrame {
    private Map<Integer, JTextField> fields = new HashMap<>();
    public TakeExamFrame(Student s, Exam e, StudentDashboard p) {
        setTitle("Taking: " + e.getSubjectName()); setSize(500, 600); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel m = new JPanel(); m.setLayout(new BoxLayout(m, BoxLayout.Y_AXIS));
        
        int i = 0;
        for(Question q : e.getQuestions()) {
            JPanel pn = new JPanel(new GridLayout(2, 1));
            pn.setBorder(BorderFactory.createTitledBorder("Q" + (i+1) + " (" + q.getScore() + " pts)"));
            pn.add(new JLabel(q.getQuestionText()));
            JTextField tf = new JTextField(); pn.add(tf);
            fields.put(i++, tf); m.add(pn);
        }
        JButton b = new JButton("Submit");
        b.addActionListener(ev -> {
            Map<Integer, String> ans = new HashMap<>();
            for(Map.Entry<Integer, JTextField> en : fields.entrySet()) ans.put(en.getKey(), en.getValue().getText());
            try {
                double sc = new StudentServiceImpl().takeExam(s, e, ans);
                JOptionPane.showMessageDialog(this, "Submitted! Score: " + sc);
                p.refresh(); dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        add(new JScrollPane(m), BorderLayout.CENTER); add(b, BorderLayout.SOUTH);
    }
}
