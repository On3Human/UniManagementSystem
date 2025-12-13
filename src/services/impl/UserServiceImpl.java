package services.impl;

import exception.ValidationException;
import models.users.User;
import services.interfaces.IUserService;
import storage.DataManager;

public class UserServiceImpl implements IUserService {
    private DataManager dataManager = DataManager.getInstance();

    @Override
    public User login(String username, String password) throws ValidationException {
        User user = dataManager.getUsers().get(username);
        if (user == null) {
            throw new ValidationException("User not found.");
        }
        if (!user.validatePassword(password)) {
            throw new ValidationException("Invalid password.");
        }
        return user;
    }

    @Override
    public void registerUser(User user) throws ValidationException {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ValidationException("Username cannot be empty.");
        }
        if (dataManager.getUsers().containsKey(user.getUsername())) {
            throw new ValidationException("User " + user.getUsername() + " already exists!");
        }
        
        dataManager.getUsers().put(user.getUsername(), user);
        dataManager.saveData(); 
        // No System.out.println here! The GUI will handle the success message.
    }

    @Override
    public User findUserByUsername(String username) {
        return dataManager.getUsers().get(username);
    }
}
