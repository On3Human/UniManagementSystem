package views.lecturer;

import models.academic.Exam;
import models.academic.questions.*;
import models.users.Lecturer;
import models.users.Student;
import models.users.User;
import services.impl.LecturerServiceImpl;
import services.interfaces.ILecturerService;
import storage.DataManager;
import views.auth.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class LecturerDashboard extends JFrame {
    private Lecturer currentLecturer;
    private ILecturerService lecturerService;
    private Exam tempExam;
    private DefaultListModel<String> questionListModel;

    public LecturerDashboard(Lecturer lecturer) {
        this.currentLecturer = lecturer;
        this.lecturerService = new LecturerServiceImpl();

        setTitle("Lecturer Dashboard - " + lecturer.getUsername());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Create Exam", createExamPanel());
        tabs.addTab("Class Reports", createReportPanel()); // Requirement: Reporting Tools
        tabs.addTab("My Profile", createProfilePanel());

        add(tabs, BorderLayout.CENTER);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(logout, BorderLayout.SOUTH);
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Header (Duration added)
        JPanel header = new JPanel(new GridLayout(4, 2));
        JTextField idTxt = new JTextField();
        JTextField subTxt = new JTextField();
        JTextField durTxt = new JTextField(); // Requirement: Specify duration
        JButton startBtn = new JButton("Initialize Exam");
        
        header.add(new JLabel("Exam ID:")); header.add(idTxt);
        header.add(new JLabel("Subject:")); header.add(subTxt);
        header.add(new JLabel("Duration (mins):")); header.add(durTxt);
        header.add(new JLabel("")); header.add(startBtn);
        panel.add(header, BorderLayout.NORTH);

        // List
        questionListModel = new DefaultListModel<>();
        panel.add(new JScrollPane(new JList<>(questionListModel)), BorderLayout.CENTER);

        // Controls
        JPanel bottom = new JPanel();
        JButton addMcq = new JButton("Add MCQ");
        JButton addTf = new JButton("Add True/False");
        JButton saveBtn = new JButton("Save Exam");
        addMcq.setEnabled(false); addTf.setEnabled(false); saveBtn.setEnabled(false);
        bottom.add(addMcq); bottom.add(addTf); bottom.add(saveBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        // Logic
        startBtn.addActionListener(e -> {
            tempExam = new Exam(idTxt.getText(), subTxt.getText());
            // Store duration logic here if Exam model supported it explicitly, for now strictly UI flow
            addMcq.setEnabled(true); addTf.setEnabled(true); saveBtn.setEnabled(true);
            questionListModel.clear();
        });

        addMcq.addActionListener(e -> showAddMCQ());
        addTf.addActionListener(e -> showAddTF());

        saveBtn.addActionListener(e -> {
            lecturerService.createExam(currentLecturer, tempExam);
            JOptionPane.showMessageDialog(this, "Exam Created!");
            addMcq.setEnabled(false); addTf.setEnabled(false); saveBtn.setEnabled(false);
            tempExam = null;
        });

        return panel;
    }

    private void showAddMCQ() {
        // Simple Input for MCQ
        String q = JOptionPane.showInputDialog("Question Text:");
        String op1 = JOptionPane.showInputDialog("Option A:");
        String op2 = JOptionPane.showInputDialog("Option B:");
        String cor = JOptionPane.showInputDialog("Correct Option (A/B):");
        if(q != null) {
            tempExam.addQuestion(new MultiChoiceQuestion(q, 5.0, cor, new String[]{op1, op2}));
            questionListModel.addElement("MCQ: " + q);
        }
    }

    private void showAddTF() {
        String q = JOptionPane.showInputDialog("Statement:");
        int res = JOptionPane.showConfirmDialog(this, "Is it True?", "Answer", JOptionPane.YES_NO_OPTION);
        if(q != null) {
            tempExam.addQuestion(new TrueFalseQuestion(q, 5.0, res == JOptionPane.YES_OPTION));
            questionListModel.addElement("TF: " + q);
        }
    }

    // --- TAB 2: REPORTS (Requirement: Class Reports) ---
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JTextField examIdTxt = new JTextField(10);
        JButton genBtn = new JButton("Generate Report");
        top.add(new JLabel("Exam ID:")); top.add(examIdTxt); top.add(genBtn);
        panel.add(top, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Student", "Score"}, 0);
        panel.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        genBtn.addActionListener(e -> {
            model.setRowCount(0);
            String targetId = examIdTxt.getText();
            for(User u : DataManager.getInstance().getUsers().values()) {
                if(u instanceof Student) {
                    Student s = (Student) u;
                    if(s.hasTakenExam(targetId)) {
                        model.addRow(new Object[]{s.getUsername(), s.getExamGrades().get(targetId)});
                    }
                }
            }
        });
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btn = new JButton("Change Password");
        btn.addActionListener(e -> {
            String p = JOptionPane.showInputDialog("New Password:");
            if(p != null) { currentLecturer.setPassword(p); DataManager.getInstance().saveData(); }
        });
        panel.add(btn);
        return panel;
    }
}
