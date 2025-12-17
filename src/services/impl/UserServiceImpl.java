package services.impl;
import exception.ValidationException;
import models.users.User;
import services.interfaces.IUserService;
import storage.DataManager;
public class UserServiceImpl implements IUserService {
    private DataManager db = DataManager.getInstance();
    public User login(String u, String p) throws ValidationException {
        User user = db.getUsers().get(u);
        if(user == null || !user.validatePassword(p)) throw new ValidationException("Invalid Credentials");
        return user;
    }
    public void registerUser(User u) throws ValidationException {
        if(db.getUsers().containsKey(u.getUsername())) throw new ValidationException("User already exists");
        db.getUsers().put(u.getUsername(), u); db.saveData();
    }
    public User findUserByUsername(String u) { return db.getUsers().get(u); }
}
