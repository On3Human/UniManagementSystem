public class User {
    final private int studentId;
    private int birthDate;
    private int age;
    private int gender;
    private int password;
    private String username;
    private String name;
    public User(int id, int birthDate, int age, int gender) {
        this.studentId = id;
        this.birthDate = birthDate;
        this.age = age;
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public int getStudentId() {
        return studentId;
    }


    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

}
