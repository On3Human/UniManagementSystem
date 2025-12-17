package models.users;
import java.io.Serializable;
// Req: User Module (Update Info except ID)
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String username;
    protected String password;
    protected UserRole role;

    public User(String id, String u, String p, UserRole r) {
        this.id = id; this.username = u; this.password = p; this.role = r;
    }
    public boolean validatePassword(String input) { return this.password.equals(input); }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    
    // Setters for Updates (ID is read-only)
    public void setPassword(String p) { this.password = p; }
    public void setUsername(String u) { this.username = u; }
    
    @Override public String toString() { return role + ": " + username; }
}
