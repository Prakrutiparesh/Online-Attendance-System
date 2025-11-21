package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Division;
import Entity.Semester;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

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

    private static final long serialVersionUID = 1L;

    @EJB
    private AdminBeanLocal abl;

    // ===== User Registration Fields =====
    private String username;
    private String password;
    private String name;
    private Date date;
    private String mobile;
    private String email;

    private static final int DEFAULT_GROUP_ID = 2;

    // ===== Course & Semester Fields =====
    private Collection<Course> courseList;
    private Collection<Integer> semesterNumbers;
    private Integer selectedCourseId;
    private Integer semesterNumber;
    private String courseName;
    private String message;
    private String successMessage;

    // ===== Semester List =====
    private Collection<Semester> semesterList = new ArrayList<>();

    // ===== Subject Fields =====
    private Integer selectedSemesterId;
    private String subjectName;
// ===== Division Fields =====
    private Integer selectedDivisionCourseId;
    private Integer selectedDivisionSemesterId;
    private String divisionName;
// ===== Student Fields =====
    private String studentName;
    private Integer studentRoll;
    private Integer studentCourseId;
    private Integer studentSemesterId;
    private Integer studentDivisionId;

    // ===== Initialization =====
    @PostConstruct
    public void init() {
        courseList = new ArrayList<>(abl.getAllCourse());
        semesterNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        semesterList = new ArrayList<>(abl.getAllSemesters()); // initialize semester list
    }

    public void insertStudent() {
        try {
            if (studentName == null || studentName.trim().isEmpty()) {
                successMessage = "Please enter student name!";
                return;
            }
            if (studentRoll == null) {
                successMessage = "Please enter roll number!";
                return;
            }
            if (studentCourseId == null) {
                successMessage = "Select a course!";
                return;
            }
            if (studentSemesterId == null) {
                successMessage = "Select a semester!";
                return;
            }
            if (studentDivisionId == null) {
                successMessage = "Select a division!";
                return;
            }

            abl.addStudent(studentName, studentRoll, studentCourseId, studentSemesterId, studentDivisionId);

            successMessage = "Student added successfully: " + studentName;

            // Reset fields
            studentName = null;
            studentRoll = null;
            studentCourseId = null;
            studentSemesterId = null;
            studentDivisionId = null;

        } catch (Exception e) {
            successMessage = "Error adding student: " + e.getMessage();
        }
    }

    public Collection<Division> getAllDivision() {
        return abl.getAllDivision();
    }

    public Collection<Semester> getSemesterList() {
        return semesterList;
    }

    public void insertSubject() {
        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                successMessage = "Please enter a subject name!";
                return;
            }
            if (selectedCourseId == null) {
                successMessage = "Please select a course!";
                return;
            }
            if (selectedSemesterId == null) {
                successMessage = "Please select a semester!";
                return;
            }

            abl.addSubject(subjectName, selectedCourseId, selectedSemesterId);

            successMessage = "Subject added successfully: " + subjectName;

            // Reset fields
            subjectName = null;
            selectedCourseId = null;
            selectedSemesterId = null;

        } catch (Exception e) {
            e.printStackTrace();
            successMessage = "Error adding subject: " + e.getMessage();
        }
    }

    public Integer getSelectedSemesterId() {
        return selectedSemesterId;
    }

    public void setSelectedSemesterId(Integer selectedSemesterId) {
        this.selectedSemesterId = selectedSemesterId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSemesterList(Collection<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    // ===== Email Validation =====
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // ===== Register New User =====
    public String insertRegiStration() {
        if (!isValidEmail(email)) {
            message = "Invalid email format!";
            System.out.println(message);
            return null;
        }
        try {
            // Directly call EJB method
            abl.registerUser(username, password, name, date, mobile, email, DEFAULT_GROUP_ID);
            message = "User registered successfully!";
            return "Login.jsf?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error during registration: " + e.getMessage();
            return null;
        }
    }

    // ===== Courses =====
    public Collection<Course> getAllCourses() {
        return abl.getAllCourse();
    }

    public void insertDivision() {
        try {
            if (divisionName == null || divisionName.trim().isEmpty()) {
                successMessage = "Please enter division name!";
                return;
            }
            if (selectedDivisionCourseId == null) {
                successMessage = "Please select a course!";
                return;
            }
            if (selectedDivisionSemesterId == null) {
                successMessage = "Please select a semester!";
                return;
            }

            // Call EJB
            abl.addDivision(divisionName, selectedDivisionSemesterId, selectedDivisionCourseId);

            successMessage = "Division added successfully: " + divisionName;

            // Reset fields
            divisionName = null;
            selectedDivisionCourseId = null;
            selectedDivisionSemesterId = null;

        } catch (Exception e) {
            e.printStackTrace();
            successMessage = "Error adding division: " + e.getMessage();
        }
    }

    public void addCourse() {
        try {
            abl.addCourse(courseName);
            message = "Course added successfully!";
            courseName = "";
            // Refresh course list
            courseList = new ArrayList<>(abl.getAllCourse());
        } catch (Exception e) {
            message = "Error adding course: " + e.getMessage();
        }
    }

    public String deleteCourse(Integer id) {
        abl.deleteCourse(id);
        courseList = new ArrayList<>(abl.getAllCourse());
        return "ViewCourse?faces-redirect=true";
    }

    // ===== Semesters =====
    public String insertSemester() {
        try {
            abl.addSemester(String.valueOf(semesterNumber), selectedCourseId);
            successMessage = "Semester added successfully!";
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            successMessage = "Something went wrong! " + e.getMessage();
            return null;
        }
    }

    // ===== Getters & Setters =====
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

    public Collection<Course> getCourseList() {
        return courseList;
    }

    public Collection<Integer> getSemesterNumbers() {
        return semesterNumbers;
    }

    public Integer getSelectedCourseId() {
        return selectedCourseId;
    }

    public void setSelectedCourseId(Integer selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public Integer getSelectedDivisionCourseId() {
        return selectedDivisionCourseId;
    }

    public void setSelectedDivisionCourseId(Integer selectedDivisionCourseId) {
        this.selectedDivisionCourseId = selectedDivisionCourseId;
    }

    public Integer getSelectedDivisionSemesterId() {
        return selectedDivisionSemesterId;
    }

    public void setSelectedDivisionSemesterId(Integer selectedDivisionSemesterId) {
        this.selectedDivisionSemesterId = selectedDivisionSemesterId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(Integer studentRoll) {
        this.studentRoll = studentRoll;
    }

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public Integer getStudentSemesterId() {
        return studentSemesterId;
    }

    public void setStudentSemesterId(Integer studentSemesterId) {
        this.studentSemesterId = studentSemesterId;
    }

    public Integer getStudentDivisionId() {
        return studentDivisionId;
    }

    public void setStudentDivisionId(Integer studentDivisionId) {
        this.studentDivisionId = studentDivisionId;
    }

}
