package services.interfaces;
import exception.ValidationException;
import models.users.User;
import java.util.List;
public interface IAdminService {
    void deleteUser(String u) throws ValidationException;
    List<User> listAllUsers();
    List<User> searchUsers(String q);
    void updateUser(User u) throws ValidationException; // Req: Update
    void assignSubjectToUser(String u, String s) throws ValidationException;
    void publishExamResults(String eid) throws ValidationException;
}
