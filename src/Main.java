import models.users.Admin;
import services.impl.UserServiceImpl;
import views.auth.LoginFrame;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        try { new UserServiceImpl().registerUser(new Admin("A1", "admin", "admin123")); } catch(Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
