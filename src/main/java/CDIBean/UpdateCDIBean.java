package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Semester;
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

    /* -----------------------------
       COURSE UPDATE VARIABLES
    ------------------------------*/
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

    @PostConstruct
    public void init() {
        courseList = (Collection<Course>) abl.getAllCourse();
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
}
