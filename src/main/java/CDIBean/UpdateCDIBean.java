package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Named("updateCDIBean")
@ViewScoped
public class UpdateCDIBean implements Serializable {

    @EJB
    AdminBeanLocal abl;

    private Integer courseId;
    private String courseName;

    /* -----------------------------
       SEMESTER UPDATE VARIABLES
    ------------------------------*/
    private Integer selectedCourseId;
    private Collection<Course> courseList;
    private Collection<Semester> semesterList;

    private boolean showUpdateForm = false;
    private Integer updateSemId;
    private String updateSemName;

// ==========================
// SUBJECT VARIABLES
// ==========================
    private Integer selectedSemId;
    private Collection<Subject> subjectList;

    private Integer updateSubId;
    private String updateSubName;

    //Division
    private Integer selectedDivisionSemId;        // For which semester we load divisions
    private Collection<Division> divisionList;

    private Integer updateDivId;
    private String updateDivName;
// ==========================
// STUDENT VARIABLES
// ==========================
    private Collection<Student> studentList;

    private Integer selectedStudentSemId;  // Semester ke hisab se students load
    private Integer selectedStudentDivId;  // Division ke hisab se students load

    private Integer updateStudId;
    private String updateStudName;
    private Integer updateStudRoll;

    @PostConstruct
    public void init() {
        courseList = (Collection<Course>) abl.getAllCourse();
        semesterList = new ArrayList<>();
        subjectList = new ArrayList<>();
        divisionList = new ArrayList<>();
        studentList = new ArrayList<>();

    }

    public void loadStudents() {
        System.out.println("selectedCourseId = " + selectedCourseId);
        System.out.println("selectedSemId = " + selectedSemId);
        System.out.println("selectedStudentDivId = " + selectedStudentDivId);

        if (selectedCourseId != null && selectedSemId != null && selectedStudentDivId != null) {
            studentList = abl.getStudentsByCourseSemDiv(selectedCourseId, selectedSemId, selectedStudentDivId);
            System.out.println("Loaded students: " + (studentList != null ? studentList.size() : 0));
        } else {
            studentList = new ArrayList<>();
        }
    }

    public void editStudent(Integer studId, String studName, Integer rollNo) {
        updateStudId = studId;
        updateStudName = studName;
        updateStudRoll = rollNo;

        showUpdateForm = true;

        System.out.println("=== EDIT STUDENT ===");
        System.out.println("ID: " + studId);
        System.out.println("Name: " + studName);
        System.out.println("Roll: " + rollNo);
    }

    public void updateStudent() {
        abl.updateStudent(updateStudId, updateStudName, updateStudRoll, selectedCourseId, selectedSemId, selectedStudentDivId);

        loadStudents();
        showUpdateForm = false;
    }

    public void deleteStudent(Integer studId) {
        abl.deleteStudent(studId, selectedCourseId, selectedSemId, selectedStudentDivId);
        loadStudents();
    }

    public void loadDivisions() {
        if (selectedCourseId != null && selectedSemId != null) {
            divisionList = abl.getDivisionsByCourseAndSemester(selectedSemId, selectedCourseId);
            System.out.println("Loaded divisions: " + (divisionList != null ? divisionList.size() : 0));
        } else {
            divisionList = new ArrayList<>();
        }
    }

    public void editDivision(Integer divId, String divName) {
        updateDivId = divId;
        updateDivName = divName;
        showUpdateForm = true;

        System.out.println("=== EDIT DIVISION ===");
        System.out.println("Div ID: " + updateDivId);
        System.out.println("Div Name: " + updateDivName);
    }

    public void updateDivision() {
        abl.updateDivision(updateDivId, updateDivName, selectedSemId, selectedCourseId);
        loadDivisions();
        showUpdateForm = false;
    }

    public void deleteDivision(Integer divId) {
        abl.deleteDivision(divId, selectedSemId, selectedCourseId);
        loadDivisions();
    }

    public void loadSubjects() {
        if (selectedCourseId != null && selectedSemId != null) {
            subjectList = abl.getSubjectsByCourseandSemester(selectedCourseId, selectedSemId);
            System.out.println("Loaded subjects: " + (subjectList != null ? subjectList.size() : 0));
        } else {
            subjectList = new ArrayList<>();
        }
    }
    // Unified save/update handler for popup

    public void cancelPopup() {
        showUpdateForm = false;
        updateSemId = null;
        updateSemName = null;
        updateSubId = null;
        updateSubName = null;

        // Division reset
        updateDivId = null;
        updateDivName = null;
        updateStudId = null;
        updateStudName = null;
        updateStudRoll = null;

    }

    public void saveUpdate() {
        if (updateSemId != null) {
            updateSemester();
        } else if (updateSubId != null) {
            updateSubject();
        } else if (updateDivId != null) {
            updateDivision();
        } else if (updateStudId != null) {
            updateStudent();
        }
    }

    // Edit subject popup
    public void editSubject(Integer subId, String subName) {
        updateSubId = subId;
        updateSubName = subName;
        showUpdateForm = true;
    }

    public void updateSubject() {
        abl.updateSubject(updateSubId, updateSubName, selectedCourseId, selectedSemId);
        loadSubjects(); // refresh list
        showUpdateForm = false;
    }

    public void deleteSubject(Integer subId) {
        abl.deleteSubject(subId, selectedCourseId, selectedSemId);
        loadSubjects(); // refresh list
    }

    public Integer getSelectedSemId() {
        return selectedSemId;
    }

    public void setSelectedSemId(Integer selectedSemId) {
        this.selectedSemId = selectedSemId;
    }

    public Collection<Subject> getSubjectList() {
        return subjectList;
    }

    public Integer getUpdateSubId() {
        return updateSubId;
    }

    public String getUpdateSubName() {
        return updateSubName;
    }

    public void setUpdateSubName(String updateSubName) {
        this.updateSubName = updateSubName;
    }

    public void cancelSubjectUpdate() {
        showUpdateForm = false;
        updateSubId = null;
        updateSubName = null;
    }

    public void loadSemesters() {
        if (selectedCourseId != null) {
            semesterList = abl.getSemestersByCourse(selectedCourseId);
            System.out.println("Loaded semesters: " + semesterList.size());
        } else {
            semesterList = null;
        }
    }

    // ✅ YEH NAYA METHOD ADD KARO - Individual parameters ke liye
    public void editSemester(Integer semId, String semName) {
        updateSemId = semId;
        updateSemName = semName;
        showUpdateForm = true;

        System.out.println("=== EDIT SEMESTER ===");
        System.out.println("Semester ID: " + updateSemId);
        System.out.println("Semester Name: " + updateSemName);
        System.out.println("Show Update Form: " + showUpdateForm);
    }

//    public void editSemester(Semester s) {
//        updateSemId = s.getSemId();
//        updateSemName = s.getSemName();
//        showUpdateForm = true;
//    }
    public String deleteSemester(Integer semId) {
        System.out.println("=== DELETE OPERATION ===");
        System.out.println("Semester ID to delete: " + semId);
        System.out.println("Selected Course ID: " + selectedCourseId);

        if (selectedCourseId != null) {
            if (semesterList != null) {
                Semester toRemove = null;
                for (Semester sem : semesterList) {
                    if (sem.getSemId().equals(semId)) {
                        toRemove = sem;
                        break;
                    }
                }
                if (toRemove != null) {
                    semesterList.remove(toRemove);
                }
            }

            abl.deleteSemester(semId, selectedCourseId);
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("courseForm:semesterTable");
        }
        return null;
    }

    public void updateSemester() {
        System.out.println("=== UPDATE SEMESTER CALLED ===");
        System.out.println("Updating - ID: " + updateSemId + ", Name: " + updateSemName + ", Course: " + selectedCourseId);

        try {
            abl.updateSemester(updateSemId, updateSemName, selectedCourseId);

            semesterList = null; // Clear old data
            loadSemesters(); // Load fresh data

            showUpdateForm = false; // ✅ YEH LINE POPUP CLOSE KAREGI

            System.out.println("Update completed - New list size: "
                    + (semesterList != null ? semesterList.size() : "null"));

            FacesContext context = FacesContext.getCurrentInstance();
            context.getPartialViewContext().getRenderIds().add("courseForm:semesterTable");
            context.getPartialViewContext().getRenderIds().add("updateForm");

        } catch (Exception e) {
            System.out.println("Error in update: " + e.getMessage());
        }
    }

    public void cancelUpdate() {
        showUpdateForm = false;
        updateSemId = null;
        updateSemName = null;
        System.out.println("Cancel update - popup closed");
    }

    public UpdateCDIBean() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cid");
        Object cname = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cname");

        if (id != null) {
            this.courseId = (Integer) id;
        }
        if (cname != null) {
            this.courseName = (String) cname;
        }
    }

    public void updateCourse() {
        if (courseId == null) {
            System.out.println("ERROR: courseId is NULL");
            return;
        }
        abl.updateCourse(courseName, courseId);
    }

    /* -----------------------------
       GETTERS & SETTERS  
    ------------------------------*/
    public Collection<Division> getDivisionList() {
        return divisionList;
    }

    public Integer getUpdateDivId() {
        return updateDivId;
    }

    public String getUpdateDivName() {
        return updateDivName;
    }

    public void setUpdateDivName(String updateDivName) {
        this.updateDivName = updateDivName;
    }

    public Integer getSelectedCourseId() {
        return selectedCourseId;
    }

    public void setSelectedCourseId(Integer selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }

    public Collection<Course> getCourseList() {
        return courseList;
    }

    public Collection<Semester> getSemesterList() {
        return semesterList;
    }

    public boolean isShowUpdateForm() {
        return showUpdateForm;
    }

    public String getUpdateSemName() {
        return updateSemName;
    }

    public void setUpdateSemName(String updateSemName) {
        this.updateSemName = updateSemName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getUpdateSemId() {
        return updateSemId;
    }

    public void setUpdateSemId(Integer updateSemId) {
        this.updateSemId = updateSemId;
    }

    public void onCourseChange() {
        // nothing to do — JSF automatically calls getSemesters()
    }

    public Collection<Student> getStudentList() {
        return studentList;
    }

    public Integer getUpdateStudId() {
        return updateStudId;
    }

    public String getUpdateStudName() {
        return updateStudName;
    }

    public void setUpdateStudName(String updateStudName) {
        this.updateStudName = updateStudName;
    }

    public Integer getUpdateStudRoll() {
        return updateStudRoll;
    }

    public void setUpdateStudRoll(Integer updateStudRoll) {
        this.updateStudRoll = updateStudRoll;
    }

    public Integer getSelectedStudentSemId() {
        return selectedStudentSemId;
    }

    public void setSelectedStudentSemId(Integer selectedStudentSemId) {
        this.selectedStudentSemId = selectedStudentSemId;
    }

    public Integer getSelectedStudentDivId() {
        return selectedStudentDivId;
    }

    public void setSelectedStudentDivId(Integer selectedStudentDivId) {
        this.selectedStudentDivId = selectedStudentDivId;
    }

}
