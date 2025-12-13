package views.auth;

import exception.ValidationException;
import models.users.*;
import services.impl.UserServiceImpl;
import services.interfaces.IUserService;
import views.admin.AdminDashboard;
import views.lecturer.LecturerDashboard;
import views.student.StudentDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userText;
    private JPasswordField passText;
    private IUserService userService;

    public LoginFrame() {
        userService = new UserServiceImpl();
        setTitle("UniSys - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("University Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Username:"));
        userText = new JTextField();
        inputPanel.add(userText);
        inputPanel.add(new JLabel("Password:"));
        passText = new JPasswordField();
        inputPanel.add(passText);
        add(inputPanel);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(e -> handleLogin());
        setVisible(true);
    }

    private void handleLogin() {
        try {
            User user = userService.login(userText.getText(), new String(passText.getPassword()));
            this.dispose();
            if (user instanceof Admin) new AdminDashboard((Admin) user).setVisible(true);
            else if (user instanceof Lecturer) new LecturerDashboard((Lecturer) user).setVisible(true);
            else if (user instanceof Student) new StudentDashboard((Student) user).setVisible(true);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
