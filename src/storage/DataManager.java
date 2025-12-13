package storage;

import models.users.User;
import models.academic.Exam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;
    private static final String FILE_PATH = "university_data.ser";

    private Map<String, User> users; // Key: Username
    private List<Exam> allExams;

    private DataManager() {
        users = new HashMap<>();
        allExams = new ArrayList<>();
        loadData();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public Map<String, User> getUsers() { return users; }
    public List<Exam> getAllExams() { return allExams; }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            oos.writeObject(allExams);
            System.out.println("[Data] Saved to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("[Data] Error saving: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) ois.readObject();
            allExams = (List<Exam>) ois.readObject();
            System.out.println("[Data] Loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Data] Error loading: " + e.getMessage());
        }
    }
}
