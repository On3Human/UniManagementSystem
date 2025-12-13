package services.interfaces;

import exception.ValidationException;
import models.users.User;

public interface IUserService {
    User login(String username, String password) throws ValidationException;
    void registerUser(User user) throws ValidationException;
    User findUserByUsername(String username);
}
