package views.student;
import exception.ValidationException;
import models.academic.Exam;
import models.users.Student;
import services.impl.StudentServiceImpl;
import storage.DataManager;
import views.auth.LoginFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StudentDashboard extends JFrame {
    private Student student;
    private StudentServiceImpl service = new StudentServiceImpl();
    private DefaultTableModel examModel, historyModel;

    public StudentDashboard(Student s) {
        this.student = s;
        setTitle("Student: " + s.getUsername()); setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Available Exams", createAvailablePanel());
        tabs.addTab("History", createHistoryPanel());
        tabs.addTab("Profile", createProfilePanel());
        add(tabs, BorderLayout.CENTER);
        JButton out = new JButton("Logout"); out.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(out, BorderLayout.SOUTH);
        refresh();
    }

    private JPanel createAvailablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Enrolled: " + student.getEnrolledSubjects(), SwingConstants.CENTER), BorderLayout.NORTH);
        examModel = new DefaultTableModel(new String[]{"ID","Subject"}, 0);
        JTable t = new JTable(examModel);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        
        JPanel b = new JPanel();
        JButton take = new JButton("Take Exam"), ref = new JButton("Refresh");
        b.add(take); b.add(ref); p.add(b, BorderLayout.SOUTH);
        
        take.addActionListener(e -> {
            int r = t.getSelectedRow();
            if(r != -1) {
                String eid = (String)examModel.getValueAt(r, 0);
                Exam ex = findExam(eid);
                if(ex != null) new TakeExamFrame(student, ex, this).setVisible(true);
            }
        });
        ref.addActionListener(e -> refresh());
        return p;
    }

    private JPanel createHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        historyModel = new DefaultTableModel(new String[]{"ID","Score", "Status"}, 0);
        JTable t = new JTable(historyModel);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        
        JPanel b = new JPanel();
        JButton v = new JButton("View Corrected"), rc = new JButton("Re-Correct"), fb = new JButton("Feedback");
        b.add(v); b.add(rc); b.add(fb); p.add(b, BorderLayout.SOUTH);
        
        v.addActionListener(e -> {
            int r = t.getSelectedRow();
            if(r != -1) {
                String status = (String)historyModel.getValueAt(r, 2);
                if(status.equals("Pending Approval")) {
                    JOptionPane.showMessageDialog(this, "Results not yet released by Admin.");
                    return;
                }
                Exam ex = findExam((String)historyModel.getValueAt(r, 0));
                if(ex != null) new ViewExamDetailsFrame(ex).setVisible(true);
            }
        });
        rc.addActionListener(e -> {
            int r = t.getSelectedRow();
            if(r != -1) {
                String s = JOptionPane.showInputDialog("Reason:");
                if(s != null) try{ service.requestReCorrection(student, (String)historyModel.getValueAt(r, 0), s); JOptionPane.showMessageDialog(this, "Sent"); }catch(Exception ex){}
            }
        });
        fb.addActionListener(e -> {
            int r = t.getSelectedRow();
            if(r != -1) {
                String s = JOptionPane.showInputDialog("Feedback:");
                if(s != null) service.submitFeedback(student, findExam((String)historyModel.getValueAt(r, 0)), s);
            }
        });
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(); JButton b = new JButton("Change Password");
        b.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("New Pass:");
            if(s != null) { student.setPassword(s); DataManager.getInstance().saveData(); }
        });
        p.add(b); return p;
    }

    public void refresh() {
        if(examModel == null || historyModel == null) return;
        examModel.setRowCount(0); historyModel.setRowCount(0);
        List<String> subs = student.getEnrolledSubjects();
        
        for(Exam e : DataManager.getInstance().getAllExams()) {
            boolean match = false;
            for(String s : subs) if(s.trim().equalsIgnoreCase(e.getSubjectName().trim())) match = true;
            if(e.isPublished() && match && !student.hasTakenExam(e.getExamId())) {
                examModel.addRow(new Object[]{e.getExamId(), e.getSubjectName()});
            }
        }
        for(Map.Entry<String, Double> en : student.getExamGrades().entrySet()) {
            Exam ex = findExam(en.getKey());
            if(ex != null) {
                if(ex.areResultsPublished()) {
                    historyModel.addRow(new Object[]{en.getKey(), en.getValue(), "Released"});
                } else {
                    historyModel.addRow(new Object[]{en.getKey(), "Hidden", "Pending Approval"});
                }
            }
        }
    }
    
    private Exam findExam(String id) {
        for(Exam e : DataManager.getInstance().getAllExams()) if(e.getExamId().equals(id)) return e;
        return null;
    }
}
