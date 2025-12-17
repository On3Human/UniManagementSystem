package views.lecturer;
import models.academic.Exam;
import models.academic.questions.*;
import models.users.*;
import services.impl.LecturerServiceImpl;
import storage.DataManager;
import views.auth.LoginFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDashboard extends JFrame {
    private Lecturer lecturer;
    private Exam tempExam;
    private DefaultListModel<String> listModel;
    private LecturerServiceImpl service = new LecturerServiceImpl();

    public LecturerDashboard(Lecturer l) {
        this.lecturer = l;
        setTitle("Lecturer: " + l.getUsername()); setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Exams", createExamPanel());
        tabs.addTab("Class Reports", createReportPanel()); // Req Lecturer.c
        tabs.addTab("My Profile", createProfilePanel());
        add(tabs);
        JButton out = new JButton("Logout"); out.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(out, BorderLayout.SOUTH);
    }

    private JPanel createExamPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(4, 2));
        JTextField id = new JTextField(), sub = new JTextField(), dur = new JTextField();
        JButton init = new JButton("Init Exam");
        top.add(new JLabel("ID:")); top.add(id); top.add(new JLabel("Subject:")); top.add(sub);
        top.add(new JLabel("Duration (mins):")); top.add(dur); top.add(init);
        p.add(top, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        p.add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton mcq = new JButton("Add MCQ"), tf = new JButton("Add T/F"), shrt = new JButton("Add Short"), save = new JButton("Save Exam"), del = new JButton("Delete Exam");
        mcq.setEnabled(false); tf.setEnabled(false); shrt.setEnabled(false); save.setEnabled(false);
        bot.add(mcq); bot.add(tf); bot.add(shrt); bot.add(save); bot.add(del);
        p.add(bot, BorderLayout.SOUTH);

        init.addActionListener(e -> {
            if(id.getText().isEmpty() || sub.getText().isEmpty()) return;
            tempExam = new Exam(id.getText(), sub.getText(), dur.getText());
            listModel.clear();
            mcq.setEnabled(true); tf.setEnabled(true); shrt.setEnabled(true); save.setEnabled(true);
        });

        // Add Question Logic
        mcq.addActionListener(e -> {
             JTextField q=new JTextField(), o1=new JTextField(), o2=new JTextField(), c=new JTextField();
             Object[] msg = {"Question:",q,"Option A:",o1,"Option B:",o2,"Correct (A/B):",c};
             if(JOptionPane.showConfirmDialog(this,msg,"MCQ",2)==0) {
                 tempExam.addQuestion(new MultiChoiceQuestion(q.getText(), 5, c.getText(), new String[]{o1.getText(), o2.getText()}));
                 listModel.addElement("MCQ: "+q.getText());
             }
        });
        tf.addActionListener(e -> {
             JTextField q=new JTextField(); JComboBox<String> c=new JComboBox<>(new String[]{"true","false"});
             Object[] msg = {"Stmt:",q,"Correct:",c};
             if(JOptionPane.showConfirmDialog(this,msg,"T/F",2)==0) {
                 tempExam.addQuestion(new TrueFalseQuestion(q.getText(), 5, Boolean.parseBoolean((String)c.getSelectedItem())));
                 listModel.addElement("T/F: "+q.getText());
             }
        });
        shrt.addActionListener(e -> {
             JTextField q=new JTextField(), c=new JTextField();
             Object[] msg = {"Question:",q,"Answer:",c};
             if(JOptionPane.showConfirmDialog(this,msg,"Short",2)==0) {
                 tempExam.addQuestion(new ShortAnswerQuestion(q.getText(), 5, c.getText()));
                 listModel.addElement("Short: "+q.getText());
             }
        });
        
        save.addActionListener(e -> {
            service.createExam(lecturer, tempExam); JOptionPane.showMessageDialog(this, "Exam Saved");
            mcq.setEnabled(false); tf.setEnabled(false); shrt.setEnabled(false); save.setEnabled(false);
            id.setText(""); sub.setText(""); dur.setText(""); listModel.clear();
        });
        
        del.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("Exam ID to Delete:");
            if(s != null) { service.deleteExam(s); JOptionPane.showMessageDialog(this, "Deleted"); }
        });
        return p;
    }

    private JPanel createReportPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(); JTextField id = new JTextField(10); JButton g = new JButton("Generate Report");
        top.add(new JLabel("Exam ID:")); top.add(id); top.add(g); p.add(top, BorderLayout.NORTH);
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Student", "Score"}, 0);
        p.add(new JScrollPane(new JTable(m)), BorderLayout.CENTER);
        
        JLabel stats = new JLabel("Stats: "); p.add(stats, BorderLayout.SOUTH);
        
        g.addActionListener(e -> {
            m.setRowCount(0);
            List<Double> scores = new ArrayList<>();
            for(User u : DataManager.getInstance().getUsers().values()) {
                if(u instanceof Student) {
                    Student s = (Student)u;
                    if(s.hasTakenExam(id.getText())) {
                        double score = s.getExamGrades().get(id.getText());
                        m.addRow(new Object[]{s.getUsername(), score});
                        scores.add(score);
                    }
                }
            }
            if(scores.size() > 0) {
                double sum = 0, max = -1, min = 9999;
                for(double d : scores) { sum+=d; if(d>max)max=d; if(d<min)min=d; }
                stats.setText(String.format("Avg: %.2f | Max: %.1f | Min: %.1f", sum/scores.size(), max, min));
            } else stats.setText("No data found");
        });
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(); JButton b = new JButton("Change Password");
        b.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("New Pass:");
            if(s != null) { lecturer.setPassword(s); DataManager.getInstance().saveData(); }
        });
        p.add(b); return p;
    }
}
