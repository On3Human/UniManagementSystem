package views.auth;

import exception.ValidationException;
import models.users.*;
import services.impl.UserServiceImpl;
import views.admin.AdminDashboard;
import views.lecturer.LecturerDashboard;
import views.student.StudentDashboard;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField uT;
    private JPasswordField pT;

    public LoginFrame() {
        setTitle("UMS Login");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title = new JLabel("University Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title);

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.add(new JLabel("Username:"));
        uT = new JTextField();
        p.add(uT);
        p.add(new JLabel("Password:"));
        pT = new JPasswordField();
        p.add(pT);
        add(p);

        JButton b = new JButton("Login");
        add(b);
        b.addActionListener(e -> login());
        setVisible(true);
    }

    private void login() {
        try {
            User u = new UserServiceImpl().login(uT.getText(), new String(pT.getPassword()));
            dispose();
            if (u instanceof Admin)
                new AdminDashboard((Admin) u).setVisible(true);
            else if (u instanceof Lecturer)
                new LecturerDashboard((Lecturer) u).setVisible(true);
            else
                new StudentDashboard((Student) u).setVisible(true);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
