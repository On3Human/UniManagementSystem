package views.admin;
import exception.ValidationException;
import models.users.*;
import models.academic.Exam; // Needed for iteration
import services.impl.*;
import views.auth.LoginFrame;
import storage.DataManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private AdminServiceImpl adminService = new AdminServiceImpl();
    private UserServiceImpl userService = new UserServiceImpl();

    public AdminDashboard(Admin a) {
        this.admin = a;
        setTitle("Admin: " + a.getUsername()); setSize(900, 650); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Users", createUserPanel());
        tabs.addTab("Subjects", createSubjectPanel());
        tabs.addTab("Exam Access", createAccessPanel());
        tabs.addTab("Approve Grades", createApprovePanel()); // NEW REQ 1.2.d
        tabs.addTab("Profile", createProfilePanel());
        add(tabs);
        JButton out = new JButton("Logout"); out.addActionListener(e -> { new LoginFrame(); dispose(); });
        add(out, BorderLayout.SOUTH);
    }

    // --- EXISTING PANELS (Re-impl for completeness) ---
    private JPanel createUserPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","User","Role"},0);
        JTable table = new JTable(model); 
        
        // Refresh Logic
        Runnable refresh = () -> {
            model.setRowCount(0);
            for(User u : adminService.listAllUsers()) model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
        };
        refresh.run();
        
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel b = new JPanel();
        JButton add=new JButton("Add"), edit=new JButton("Edit"), del=new JButton("Delete");
        b.add(add); b.add(edit); b.add(del); p.add(b, BorderLayout.SOUTH);

        add.addActionListener(e -> showUserDialog(null, refresh));
        edit.addActionListener(e -> {
            int r=table.getSelectedRow();
            if(r!=-1) showUserDialog(userService.findUserByUsername((String)model.getValueAt(r,1)), refresh);
        });
        del.addActionListener(e -> {
            int r=table.getSelectedRow();
            if(r!=-1) try{ adminService.deleteUser((String)model.getValueAt(r,1)); refresh.run(); }
            catch(Exception ex){}
        });
        return p;
    }

    private void showUserDialog(User existing, Runnable onSave) {
        JDialog d = new JDialog(this, existing==null?"Add":"Edit", true);
        d.setSize(300,350); d.setLayout(new GridLayout(6,2));
        JTextField id=new JTextField(), user=new JTextField(), pass=new JTextField();
        JComboBox<String> role = new JComboBox<>(new String[]{"STUDENT","LECTURER"});
        if(existing!=null) {
            id.setText(existing.getId()); id.setEditable(false);
            user.setText(existing.getUsername());
            role.setSelectedItem(existing.getRole().toString()); role.setEnabled(false);
        }
        d.add(new JLabel("ID:")); d.add(id); d.add(new JLabel("User:")); d.add(user);
        d.add(new JLabel("Pass:")); d.add(pass); d.add(new JLabel("Role:")); d.add(role);
        JButton s = new JButton("Save");
        s.addActionListener(e->{
            try {
                if(existing==null) {
                    User u;
                    if(role.getSelectedItem().equals("STUDENT")) u=new Student(id.getText(), user.getText(), pass.getText());
                    else u=new Lecturer(id.getText(), user.getText(), pass.getText());
                    userService.registerUser(u);
                } else {
                    if(!user.getText().isEmpty()) existing.setUsername(user.getText());
                    if(!pass.getText().isEmpty()) existing.setPassword(pass.getText());
                    adminService.updateUser(existing);
                }
                onSave.run(); d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, ex.getMessage()); }
        });
        d.add(s); d.setVisible(true);
    }

    private JPanel createSubjectPanel() {
        JPanel p = new JPanel(new FlowLayout());
        JTextField u=new JTextField(10), s=new JTextField(10); JButton b=new JButton("Assign");
        p.add(new JLabel("User:")); p.add(u); p.add(new JLabel("Subject:")); p.add(s); p.add(b);
        b.addActionListener(e->{
            try{ adminService.assignSubjectToUser(u.getText(), s.getText()); JOptionPane.showMessageDialog(this,"Assigned"); }
            catch(Exception ex){ JOptionPane.showMessageDialog(this,ex.getMessage()); }
        });
        return p;
    }

    // --- REQ: PUBLISH FOR ACCESS ---
    private JPanel createAccessPanel() {
        JPanel p = new JPanel(new FlowLayout());
        JTextField id=new JTextField(10); JButton b=new JButton("Publish Access");
        p.add(new JLabel("Exam ID:")); p.add(id); p.add(b);
        b.addActionListener(e->{
            try{ adminService.publishExamAccess(id.getText()); JOptionPane.showMessageDialog(this,"Exam is now LIVE for students."); }
            catch(Exception ex){ JOptionPane.showMessageDialog(this,ex.getMessage()); }
        });
        return p;
    }

    // --- REQ 1.2.d: APPROVE GRADES ---
    private JPanel createApprovePanel() {
        JPanel p = new JPanel(new BorderLayout());
        
        JPanel top = new JPanel();
        JTextField idTxt = new JTextField(10);
        JButton loadBtn = new JButton("Review Grades");
        JButton pubBtn = new JButton("Approve & Publish Results");
        top.add(new JLabel("Exam ID:")); top.add(idTxt); top.add(loadBtn); top.add(pubBtn);
        p.add(top, BorderLayout.NORTH);
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Student", "Score"}, 0);
        p.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        
        loadBtn.addActionListener(e -> {
            model.setRowCount(0);
            for(User u : DataManager.getInstance().getUsers().values()) {
                if(u instanceof Student && ((Student)u).hasTakenExam(idTxt.getText())) {
                    model.addRow(new Object[]{u.getUsername(), ((Student)u).getExamGrades().get(idTxt.getText())});
                }
            }
        });
        
        pubBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? Students will see their grades immediately.");
            if(confirm == JOptionPane.YES_OPTION) {
                try { 
                    adminService.publishExamResults(idTxt.getText()); 
                    JOptionPane.showMessageDialog(this, "Results Published!"); 
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });
        
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(); JButton b = new JButton("Update Password");
        b.addActionListener(e -> {
            String s=JOptionPane.showInputDialog("New Pass:");
            if(s!=null) { admin.setPassword(s); try{adminService.updateUser(admin);}catch(Exception ex){} }
        });
        p.add(b); return p;
    }
}
