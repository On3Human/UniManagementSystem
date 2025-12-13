package models.users;

public class Admin extends User {
    public Admin(String id, String username, String password) {
        super(id, username, password, UserRole.ADMIN);
    }
}
