package views.admin;

import exception.ValidationException;
import models.users.*;
import services.impl.AdminServiceImpl;
import services.impl.UserServiceImpl;
import services.interfaces.IAdminService;
import services.interfaces.IUserService;
import storage.DataManager;
import views.auth.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private Admin currentAdmin;
    private IAdminService adminService;
    private IUserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;
        this.adminService = new AdminServiceImpl();
        this.userService = new UserServiceImpl();

        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Users", createManageUsersPanel());
        tabs.addTab("Assign Subjects", createSubjectPanel());
        tabs.addTab("Publish Results", createPublishPanel());
        tabs.addTab("My Profile", createProfilePanel());

        add(tabs, BorderLayout.CENTER);
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(logout, BorderLayout.SOUTH);
    }

    // --- TAB 1: MANAGE USERS (Add, Search, Edit, Delete) ---
    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Search Bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Reset");
        topPanel.add(new JLabel("Search:")); topPanel.add(searchField); topPanel.add(searchBtn); topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Username", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        refreshUserTable(null);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        // CRUD Buttons
        JPanel bottomPanel = new JPanel();
        JButton addBtn = new JButton("Add User");
        JButton editBtn = new JButton("Edit User"); // New Requirement
        JButton delBtn = new JButton("Delete User");
        bottomPanel.add(addBtn); bottomPanel.add(editBtn); bottomPanel.add(delBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        searchBtn.addActionListener(e -> refreshUserTable(searchField.getText()));
        refreshBtn.addActionListener(e -> { searchField.setText(""); refreshUserTable(null); });
        
        addBtn.addActionListener(e -> showUserForm(null)); // Null means Add Mode
        editBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if(row == -1) return;
            String username = (String) tableModel.getValueAt(row, 1);
            User user = userService.findUserByUsername(username);
            if(user != null) showUserForm(user); // Pass user for Edit Mode
        });

        delBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if(row != -1) {
                String u = (String) tableModel.getValueAt(row, 1);
                try { adminService.deleteUser(u); refreshUserTable(null); } 
                catch (ValidationException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });

        return panel;
    }

    private void refreshUserTable(String query) {
        tableModel.setRowCount(0);
        List<User> users = (query == null || query.isEmpty()) ? adminService.listAllUsers() : adminService.searchUsers(query);
        for (User u : users) tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
    }

    private void showUserForm(User existingUser) {
        JDialog d = new JDialog(this, existingUser == null ? "Add User" : "Edit User", true);
        d.setSize(300, 300);
        d.setLayout(new GridLayout(6, 2));

        JTextField idTxt = new JTextField(existingUser != null ? existingUser.getId() : "");
        JTextField uTxt = new JTextField(existingUser != null ? existingUser.getUsername() : "");
        JPasswordField pTxt = new JPasswordField(); // Password usually reset, not shown
        String[] roles = {"STUDENT", "LECTURER", "ADMIN"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        
        if(existingUser != null) {
            idTxt.setEditable(false); // ID cannot be changed
            roleBox.setSelectedItem(existingUser.getRole().toString());
            roleBox.setEnabled(false); // Changing role is complex, simplified here
        }

        d.add(new JLabel("ID:")); d.add(idTxt);
        d.add(new JLabel("Username:")); d.add(uTxt);
        d.add(new JLabel("New Password:")); d.add(pTxt);
        d.add(new JLabel("Role:")); d.add(roleBox);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            try {
                if (existingUser == null) {
                    // Add Logic
                    User newUser;
                    if (roleBox.getSelectedItem().equals("STUDENT")) newUser = new Student(idTxt.getText(), uTxt.getText(), new String(pTxt.getPassword()));
                    else if (roleBox.getSelectedItem().equals("LECTURER")) newUser = new Lecturer(idTxt.getText(), uTxt.getText(), new String(pTxt.getPassword()));
                    else newUser = new Admin(idTxt.getText(), uTxt.getText(), new String(pTxt.getPassword()));
                    userService.registerUser(newUser);
                } else {
                    // Update Logic (Requirement 1.2.b)
                    if (!uTxt.getText().isEmpty()) { 
                         // Note: In a real app we'd need setters, but DataManager holds references
                         // so modifying fields is tricky without setters in the Abstract class.
                         // For this scope, we allow Password reset.
                         String newPass = new String(pTxt.getPassword());
                         if(!newPass.isEmpty()) existingUser.setPassword(newPass);
                         DataManager.getInstance().saveData(); // Persist changes
                    }
                }
                refreshUserTable(null);
                d.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(d, ex.getMessage()); }
        });
        d.add(save);
        d.setVisible(true);
    }

    // --- TAB 2: ASSIGN SUBJECTS ---
    private JPanel createSubjectPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField uTxt = new JTextField(); JTextField sTxt = new JTextField();
        JButton btn = new JButton("Assign");
        panel.add(new JLabel("Username:")); panel.add(uTxt);
        panel.add(new JLabel("Subject:")); panel.add(sTxt);
        panel.add(new JLabel("")); panel.add(btn);
        btn.addActionListener(e -> {
            try { adminService.assignSubjectToUser(uTxt.getText(), sTxt.getText()); JOptionPane.showMessageDialog(this, "Success"); }
            catch (ValidationException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return panel;
    }

    // --- TAB 3: PUBLISH ---
    private JPanel createPublishPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField idTxt = new JTextField(15);
        JButton btn = new JButton("Publish Exam");
        panel.add(new JLabel("Exam ID:")); panel.add(idTxt); panel.add(btn);
        btn.addActionListener(e -> {
            try { adminService.publishExamResults(idTxt.getText()); JOptionPane.showMessageDialog(this, "Published!"); }
            catch (ValidationException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return panel;
    }

    // --- TAB 4: PROFILE (Requirement 1.2.a - Alter Credentials) ---
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JPasswordField pass = new JPasswordField();
        JButton updateBtn = new JButton("Update Password");
        panel.add(new JLabel("New Password:")); panel.add(pass);
        panel.add(new JLabel("")); panel.add(updateBtn);
        
        updateBtn.addActionListener(e -> {
            String p = new String(pass.getPassword());
            if(!p.isEmpty()) {
                currentAdmin.setPassword(p);
                DataManager.getInstance().saveData();
                JOptionPane.showMessageDialog(this, "Credentials Updated");
            }
        });
        return panel;
    }
}
