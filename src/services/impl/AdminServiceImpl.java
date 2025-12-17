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
        return db.getUsers().values().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public void updateUser(User u) throws ValidationException {
        if(!db.getUsers().containsKey(u.getUsername())) throw new ValidationException("User not found in DB");
        db.saveData(); // Save changes made to reference
    }
    
    public void assignSubjectToUser(String u, String s) throws ValidationException {
        User user = db.getUsers().get(u);
        if(user == null) throw new ValidationException("User not found");
        
        boolean changed = false;
        if(user instanceof Student) { ((Student)user).enrollSubject(s.trim()); changed = true; }
        else if(user instanceof Lecturer) { ((Lecturer)user).assignSubject(s.trim()); changed = true; }
        else throw new ValidationException("Invalid role for subjects");
        
        if(changed) db.saveData();
    }
    
    public void publishExamResults(String eid) throws ValidationException {
        Exam e = db.getAllExams().stream().filter(x -> x.getExamId().equals(eid)).findFirst().orElse(null);
        if(e == null) throw new ValidationException("Exam ID not found");
        e.setPublished(true); db.saveData();
    }
}
