package storage;
import models.users.User;
import models.academic.Exam;
import java.io.*;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    private static final String FILE_PATH = "university_data.ser";
    private Map<String, User> users; 
    private List<Exam> allExams;

    private DataManager() {
        users = new HashMap<>(); allExams = new ArrayList<>();
        loadData();
    }
    public static DataManager getInstance() { if(instance==null) instance=new DataManager(); return instance; }
    public Map<String, User> getUsers() { return users; }
    public List<Exam> getAllExams() { return allExams; }

    public void saveData() {
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users); oos.writeObject(allExams);
        } catch(Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File f = new File(FILE_PATH); if(!f.exists()) return;
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f))) {
            users=(Map<String,User>)ois.readObject(); allExams=(List<Exam>)ois.readObject();
        } catch(Exception e) { e.printStackTrace(); }
    }
}
