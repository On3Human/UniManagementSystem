package views.admin;
import exception.ValidationException;
import models.users.*;
import services.impl.*;
import views.auth.LoginFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private DefaultTableModel model;
    private JTable table;
    private AdminServiceImpl adminService = new AdminServiceImpl();
    private UserServiceImpl userService = new UserServiceImpl();

    public AdminDashboard(Admin a) {
        this.admin = a;
        setTitle("Admin: " + a.getUsername()); setSize(850, 600); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Users", createUserPanel()); // Req 1.2.b
        tabs.addTab("Assign Subjects", createSubjectPanel()); // Req 1.2.c
        tabs.addTab("Publish Exams", createPublishPanel()); // Req 1.2.d
        tabs.addTab("My Profile", createProfilePanel()); // Req User.b
        add(tabs);
        JButton out = new JButton("Logout"); out.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(out, BorderLayout.SOUTH);
    }

    private JPanel createUserPanel() {
        JPanel p = new JPanel(new BorderLayout());
        model = new DefaultTableModel(new String[]{"ID","User","Role"},0);
        table = new JTable(model); refreshTable();
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel b = new JPanel();
        JButton add = new JButton("Add User"), edit = new JButton("Edit User"), del = new JButton("Delete User");
        b.add(add); b.add(edit); b.add(del); p.add(b, BorderLayout.SOUTH);
        
        add.addActionListener(e -> showUserDialog(null));
        
        // Req: Update User
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                String u = (String)model.getValueAt(r, 1);
                showUserDialog(userService.findUserByUsername(u));
            } else JOptionPane.showMessageDialog(this, "Select a user row first.");
        });
        
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) try { adminService.deleteUser((String)model.getValueAt(r, 1)); refreshTable(); }
            catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return p;
    }
    
    private void showUserDialog(User existing) {
        JDialog d = new JDialog(this, existing==null?"Add":"Edit", true);
        d.setSize(300, 350); d.setLayout(new GridLayout(6, 2));
        JTextField id = new JTextField(), user = new JTextField(), pass = new JTextField();
        JComboBox<String> role = new JComboBox<>(new String[]{"STUDENT", "LECTURER"});
        
        if(existing != null) {
            id.setText(existing.getId()); id.setEditable(false); // ID cannot be changed
            user.setText(existing.getUsername()); 
            role.setSelectedItem(existing.getRole().toString()); role.setEnabled(false);
        }
        
        d.add(new JLabel("ID:")); d.add(id);
        d.add(new JLabel("Username:")); d.add(user);
        d.add(new JLabel("Password:")); d.add(pass);
        d.add(new JLabel("Role:")); d.add(role);
        
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            try {
                if(existing == null) {
                    User u;
                    if(role.getSelectedItem().equals("STUDENT")) u = new Student(id.getText(), user.getText(), pass.getText());
                    else u = new Lecturer(id.getText(), user.getText(), pass.getText());
                    userService.registerUser(u);
                } else {
                    if(!user.getText().isEmpty()) existing.setUsername(user.getText());
                    if(!pass.getText().isEmpty()) existing.setPassword(pass.getText());
                    adminService.updateUser(existing);
                }
                refreshTable(); d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, ex.getMessage()); }
        });
        d.add(save); d.setVisible(true);
    }
    
    private void refreshTable() {
        model.setRowCount(0);
        for(User u : adminService.listAllUsers()) model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
    }
    
    private JPanel createSubjectPanel() {
        JPanel p = new JPanel(new FlowLayout());
        JTextField u = new JTextField(10), s = new JTextField(10); JButton b = new JButton("Assign Subject");
        p.add(new JLabel("Username:")); p.add(u); p.add(new JLabel("Subject:")); p.add(s); p.add(b);
        b.addActionListener(e -> {
            try { adminService.assignSubjectToUser(u.getText(), s.getText()); JOptionPane.showMessageDialog(this, "Assigned"); }
            catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return p;
    }
    
    private JPanel createPublishPanel() {
        JPanel p = new JPanel(new FlowLayout());
        JTextField id = new JTextField(10); JButton b = new JButton("Publish Exam ID");
        p.add(new JLabel("Exam ID:")); p.add(id); p.add(b);
        b.addActionListener(e -> {
            try { adminService.publishExamResults(id.getText()); JOptionPane.showMessageDialog(this, "Published"); }
            catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return p;
    }
    
    private JPanel createProfilePanel() {
        JPanel p = new JPanel(); JButton b = new JButton("Change My Password");
        b.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("New Password:");
            if(s != null) { admin.setPassword(s); try { adminService.updateUser(admin); JOptionPane.showMessageDialog(this, "Saved"); } catch(Exception ex){} }
        });
        p.add(b); return p;
    }
}
