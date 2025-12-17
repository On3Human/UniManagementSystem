package services.interfaces;
import exception.ValidationException;
import models.users.User;
public interface IUserService {
    User login(String u, String p) throws ValidationException;
    void registerUser(User u) throws ValidationException;
    User findUserByUsername(String u);
}
