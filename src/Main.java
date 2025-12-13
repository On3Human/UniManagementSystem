import models.users.Admin;
import services.impl.UserServiceImpl;
import views.auth.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure Admin exists for first run
        try {
            new UserServiceImpl().registerUser(new Admin("A001", "admin", "admin123"));
        } catch (Exception e) {
            // Already exists, ignore
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
