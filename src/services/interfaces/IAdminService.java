package services.interfaces;

import exception.ValidationException;
import models.academic.Exam;
import models.users.User;
import java.util.List;

public interface IAdminService {
    // b) User Management
    void deleteUser(String username) throws ValidationException;
    List<User> listAllUsers();
    List<User> searchUsers(String query);
    void updateUser(User user) throws ValidationException;

    // c) Subject Management
    void assignSubjectToUser(String username, String subjectName) throws ValidationException;

    // d) Grade Approval
    void publishExamResults(String examId) throws ValidationException;
}
