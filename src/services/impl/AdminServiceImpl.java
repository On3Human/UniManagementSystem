package services.impl;

import exception.ValidationException;
import models.academic.Exam;
import models.users.Lecturer;
import models.users.Student;
import models.users.User;
import services.interfaces.IAdminService;
import storage.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminServiceImpl implements IAdminService {
    private DataManager dataManager = DataManager.getInstance();

    @Override
    public void deleteUser(String username) throws ValidationException {
        if (!dataManager.getUsers().containsKey(username)) {
            throw new ValidationException("User not found: " + username);
        }
        dataManager.getUsers().remove(username);
        dataManager.saveData();
    }

    @Override
    public List<User> listAllUsers() {
        return new ArrayList<>(dataManager.getUsers().values());
    }

    @Override
    public List<User> searchUsers(String query) {
        String lowerQuery = query.toLowerCase();
        return dataManager.getUsers().values().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) || 
                             u.getId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(User user) throws ValidationException {
        // Since Objects are passed by reference, modifying the User object 
        // in the GUI often updates it in memory. 
        // We just need to ensure it's saved to the file.
        if (!dataManager.getUsers().containsKey(user.getUsername())) {
            throw new ValidationException("Cannot update: User does not exist.");
        }
        dataManager.saveData();
    }

    @Override
    public void assignSubjectToUser(String username, String subjectName) throws ValidationException {
        User user = dataManager.getUsers().get(username);
        if (user == null) {
            throw new ValidationException("User not found.");
        }
        
        if (subjectName == null || subjectName.isEmpty()) {
            throw new ValidationException("Subject name cannot be empty.");
        }

        boolean changed = false;
        if (user instanceof Student) {
            ((Student) user).enrollSubject(subjectName);
            changed = true;
        } else if (user instanceof Lecturer) {
            ((Lecturer) user).assignSubject(subjectName);
            changed = true;
        } else {
            throw new ValidationException("Subjects can only be assigned to Students or Lecturers.");
        }

        if (changed) {
            dataManager.saveData();
        }
    }

    @Override
    public void publishExamResults(String examId) throws ValidationException {
        Exam targetExam = null;
        for (Exam e : dataManager.getAllExams()) {
            if (e.getExamId().equals(examId)) {
                targetExam = e;
                break;
            }
        }

        if (targetExam == null) {
            throw new ValidationException("Exam ID not found.");
        }

        targetExam.setPublished(true);
        dataManager.saveData();
    }
}
