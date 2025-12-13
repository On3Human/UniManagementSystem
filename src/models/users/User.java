package models.users;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String id;
    protected String username;
    protected String password;
    protected UserRole role;

    public User(String id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean validatePassword(String inputPassword) {
        // In a real app, hash checking happens here
        return this.password.equals(inputPassword);
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return role + ": " + username + " (" + id + ")";
    }
}
