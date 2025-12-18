package services.impl;
import exception.ValidationException;
import models.academic.Exam;
import models.users.*;
import services.interfaces.IAdminService;
import storage.DataManager;
import java.util.*;
import java.util.stream.Collectors;

public class AdminServiceImpl implements IAdminService {
    private DataManager db = DataManager.getInstance();
    
    public void deleteUser(String u) throws ValidationException {
        if(!db.getUsers().containsKey(u)) throw new ValidationException("User not found");
        db.getUsers().remove(u); db.saveData();
    }
    public List<User> listAllUsers() { return new ArrayList<>(db.getUsers().values()); }
    public List<User> searchUsers(String q) {
        return db.getUsers().values().stream().filter(u->u.getUsername().toLowerCase().contains(q.toLowerCase())).collect(Collectors.toList());
    }
    public void updateUser(User u) throws ValidationException {
        if(!db.getUsers().containsKey(u.getUsername())) throw new ValidationException("User not found");
        db.saveData();
    }
    public void assignSubjectToUser(String u, String s) throws ValidationException {
        User user = db.getUsers().get(u);
        if(user==null) throw new ValidationException("User not found");
        if(user instanceof Student) ((Student)user).enrollSubject(s.trim());
        else if(user instanceof Lecturer) ((Lecturer)user).assignSubject(s.trim());
        else throw new ValidationException("Invalid Role");
        db.saveData();
    }
    
    // 1. Publish for Access (Students can TAKE it)
    public void publishExamAccess(String eid) throws ValidationException {
        Exam e = findExam(eid);
        e.setPublished(true); 
        db.saveData();
    }
    
    // 2. Publish for Results (Students can SEE grades)
    public void publishExamResults(String eid) throws ValidationException {
        Exam e = findExam(eid);
        e.setResultsPublished(true); 
        db.saveData();
    }
    
    private Exam findExam(String eid) throws ValidationException {
        Exam e = db.getAllExams().stream().filter(x -> x.getExamId().equals(eid)).findFirst().orElse(null);
        if(e == null) throw new ValidationException("Exam ID not found");
        return e;
    }
}
