import exception.ValidationException;
import models.academic.Exam;
import models.academic.questions.MultiChoiceQuestion;
import models.academic.questions.TrueFalseQuestion;
import models.users.Lecturer;
import models.users.User;
import models.users.Student;
import services.impl.LecturerServiceImpl;
import services.impl.UserServiceImpl;
import services.interfaces.ILecturerService;
import services.interfaces.IUserService;

public class Main {
    public static void main(String[] args) {
        IUserService userService = new UserServiceImpl();
        ILecturerService lecService = new LecturerServiceImpl();

        System.out.println("--- University Management System Initialized ---");

        // 1. Register Users (Try/Catch simulates GUI behavior)
        try {
            Lecturer lec = new Lecturer("L001", "prof_java", "pass123");
            userService.registerUser(lec);
            System.out.println("[GUI Success] Registered Lecturer: prof_java");

            Student stu = new Student("S001", "student_bob", "bob123");
            userService.registerUser(stu);
            System.out.println("[GUI Success] Registered Student: student_bob");
        } catch (ValidationException e) {
            // This represents a Popup Error Box in Swing
            System.out.println("[GUI Error Popup] " + e.getMessage());
        }

        // 2. Login (Try/Catch)
        User loggedIn = null;
        try {
            loggedIn = userService.login("prof_java", "pass123");
            System.out.println("[GUI Success] Login Verified: " + loggedIn.getUsername());
        } catch (ValidationException e) {
            System.out.println("[GUI Error Popup] Login Failed: " + e.getMessage());
        }

        // 3. Create Exam
        if(loggedIn != null && loggedIn instanceof Lecturer) {
            Lecturer myLec = (Lecturer) loggedIn;
            
            Exam javaExam = new Exam("E01", "Java 101");
            javaExam.addQuestion(new MultiChoiceQuestion("Is Java OOP?", 5.0, "A", new String[]{"A. Yes", "B. No"}));
            javaExam.addQuestion(new TrueFalseQuestion("Is int an Object?", 5.0, false));
            
            lecService.createExam(myLec, javaExam);
            System.out.println("[GUI Success] Exam Created and Saved.");
        }
    }
}
