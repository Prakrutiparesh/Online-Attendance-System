package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Subject;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("adminCDIBean")
@ViewScoped
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

    private Integer groupId;

    // ===== Course & Semester Fields =====
    private Collection<Course> courseList;
    private Collection<Integer> semesterNumbers;
    private Collection<Division> divisionList = new ArrayList<>();
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
    private Integer selectedDivisionId; // <-- added
    private String divisionName;

    // ===== Student Fields =====
    private String studentName;
    private Integer studentRoll;
    private Integer studentCourseId;
    private Integer studentSemesterId;
    private Integer studentDivisionId;
//    private Integer selectedSemId;
//    private Integer updateSubId;
//    private Integer selectedStudentDivId;
    private Collection<Subject> subjectList;

    // ===== Initialization =====
    @PostConstruct
    public void init() {
        String groupParam = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("group_id");

        if (groupParam != null) {
            groupId = Integer.parseInt(groupParam);
        } else {
            groupId = 2;
        }

        courseList = new ArrayList<>(abl.getAllCourse());
        semesterNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        semesterList = new ArrayList<>(abl.getAllSemesters());
        divisionList = new ArrayList<>();
        subjectList = new ArrayList<>();

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
            successMessage = "Subject added successfully: " + subjectName; // Reset fields subjectName = null;
            selectedCourseId = null;
            selectedSemesterId = null;
        } catch (Exception e) {
            e.printStackTrace();
            successMessage = "Error adding subject: " + e.getMessage();
        }
    }

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

    // ===== Student Methods =====
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
            e.printStackTrace(); // Log actual problem to server console

            successMessage = "Error adding student: " + e.getMessage();
        }
    }

    // ===== Division Methods =====
    public Collection<Division> getAllDivision() {
        return abl.getAllDivision();
    }

    public Collection<Division> getDivisionsBySemester(Integer semesterId) {
        if (semesterId == null) {
            return new ArrayList<>();
        }
        Collection<Division> divisionsBySemester = new ArrayList<>();
        for (Division d : abl.getAllDivision()) {
            if (d.getSemester().getSemId().equals(semesterId)) {
                divisionsBySemester.add(d);
            }
        }
        return divisionsBySemester;
    }

    // ===== Semester Methods =====
    public Collection<Semester> getSemesterList() {
        return semesterList;
    }

    public Collection<Semester> getSemestersByCourse(Integer courseId) {
        if (courseId == null) {
            return new ArrayList<>();
        }
        Collection<Semester> result = new ArrayList<>();
        for (Semester s : semesterList) {
            if (s.getCourse().getCourseId().equals(courseId)) {
                result.add(s);
            }
        }
        return result;
    }

    // ===== Course Methods =====
    public Collection<Course> getAllCourses() {
        return abl.getAllCourse();
    }

    public void addCourse() {
        try {
            abl.addCourse(courseName);
            message = "Course added successfully!";
            courseName = ""; // Refresh course list
            courseList = new ArrayList<>(abl.getAllCourse());
        } catch (Exception e) {
            message = "Error adding course: " + e.getMessage();
        }
    }

    public void insertDivision() {
        try {
            if (divisionName == null || divisionName.trim().isEmpty()) {
                successMessage = "Please enter division name!";
                return;
            }
            if (selectedCourseId == null) {
                successMessage = "Please select a course!";
                return;
            }
            if (selectedDivisionSemesterId == null) {
                successMessage = "Please select a semester!";
                return;
            }
            System.out.println("going here sdivision");
            // Call EJB
            abl.addDivision(divisionName, selectedDivisionSemesterId, selectedCourseId);

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

    public String deleteCourse(Integer id) {
        abl.deleteCourse(id);
        courseList = new ArrayList<>(abl.getAllCourse());
        return "ViewCourse?faces-redirect=true";
    }

    public String getSelectedCourseName() {
        for (Course c : courseList) {
            if (c.getCourseId().equals(selectedCourseId)) {
                return c.getCourseName();
            }
        }
        return null;
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
            abl.registerUser(username, password, name, date, mobile, email, groupId);
            message = "User registered successfully!";
            return "Login.jsf?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error during registration: " + e.getMessage();
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

    public Integer getSelectedDivisionId() {
        return selectedDivisionId;
    } // <-- added

    public void setSelectedDivisionId(Integer selectedDivisionId) {
        this.selectedDivisionId = selectedDivisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
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

    public void onCourseChange() {
        if (studentCourseId != null) {
            semesterList = new ArrayList<>(abl.getSemestersByCourse(studentCourseId));
        } else {
            semesterList = new ArrayList<>();
        }
        studentSemesterId = null;
        studentDivisionId = null;
    }

    public void onSemesterChange() {
        if (studentSemesterId != null) {
            divisionList = new ArrayList<>(abl.getDivisionsBySemester(studentSemesterId));
        } else {
            divisionList = new ArrayList<>();
        }
        studentDivisionId = null;
    }

    public void loadSemesters() {
        studentSemesterId = null;
        studentDivisionId = null;
        divisionList = new ArrayList<>();

        if (studentCourseId != null) {
            semesterList = abl.getSemestersByCourse(studentCourseId);
        } else {
            semesterList = new ArrayList<>();
        }
    }

    public void loadDivisions() {
        if (studentSemesterId != null) {
            divisionList = new ArrayList<>(abl.getDivisionsBySemester(studentSemesterId));
        } else {
            divisionList = new ArrayList<>();
        }
    }

    public Collection<Division> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Collection<Division> divisionList) {
        this.divisionList = divisionList;
    }

}
