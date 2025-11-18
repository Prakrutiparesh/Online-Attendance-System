package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Users;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.GenericType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("adminCDIBean")
@RequestScoped
public class AdminCDIBean implements Serializable {

    @EJB
    AdminBeanLocal abl;

    // Users
    Collection<Users> user;
    GenericType<Collection<Users>> guser;

    String username;
    String password;
    String name;
    Date date;
    String mobile;
    String email;

    public AdminCDIBean() {
        user = new ArrayList<>();
        guser = new GenericType<Collection<Users>>() {
        };
    }

    // Email validation
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Register new admin/user
    public String insertRegiStration() {
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format");
            return null;
        }
        try {
            abl.registerUser(username, password, name, date, mobile, email, 2);
            return "Login.jsf";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//Course
    private String message;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMessage() {
        return message;
    }

    // Get all courses
    public Collection<Course> getAllCourses() {
        return abl.getAllCourse();
    }

    // Delete course
    public String deleteCourse(Integer id) {
        abl.deleteCourse(id);
        return "ViewCourse?faces-redirect=true";
    }

    public void addCourse() {
        try {
            abl.addCourse(courseName);
            message = "Course added suSccessfully!";
            courseName = ""; // Clear form
        } catch (Exception e) {
            message = "Error: " + e.getMessage();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Semester
    private Integer selectedCourseId;
    private Integer semesterNumber;

    private Collection<Course> courseList;
    private Collection<Integer> semesterNumbers;
    private String successMessage;

    @PostConstruct
    public void init() {
        courseList = new ArrayList<>(abl.getAllCourse());
        semesterNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    public String insertSemester() {
        try {
            abl.addSemester(String.valueOf(semesterNumber), selectedCourseId);
            successMessage = "Semester Added Successfully!";
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            successMessage = "Something went wrong!";
            return null;
        }
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public Integer getSelectedCourseId() {
        return selectedCourseId;
    }

    public void setSelectedCourseId(Integer id) {
        this.selectedCourseId = id;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer n) {
        this.semesterNumber = n;
    }

    public Collection<Course> getCourseList() {
        return courseList;
    }

    public Collection<Integer> getSemesterNumbers() {
        return semesterNumbers;
    }
}
