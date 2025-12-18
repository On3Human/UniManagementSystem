package services.interfaces;
import exception.ValidationException;
import models.users.User;
import java.util.List;
public interface IAdminService {
    void deleteUser(String u) throws ValidationException;
    List<User> listAllUsers();
    List<User> searchUsers(String q);
    void updateUser(User u) throws ValidationException;
    void assignSubjectToUser(String u, String s) throws ValidationException;
    void publishExamAccess(String eid) throws ValidationException; // For taking exam
    void publishExamResults(String eid) throws ValidationException; // For viewing grades
}
