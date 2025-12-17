package models.users;
public class Admin extends User {
    public Admin(String id, String u, String p) { super(id, u, p, UserRole.ADMIN); }
}
