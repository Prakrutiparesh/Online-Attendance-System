package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Course;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("updateCDIBean")
@ViewScoped
public class UpdateCDIBean implements Serializable {

    @EJB
    AdminBeanLocal abl;

    private Integer courseId;
    private String courseName;

    public UpdateCDIBean() {
        // Flash se value lane ka logic
        Object id = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cid");
        Object cname = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("cname");

        if (id != null) {
            this.courseId = (Integer) id;
        }
        if (cname != null) {
            this.courseName = (String) cname;
        }
    }

    // POPUP SE VALUE SET KARNE KA METHOD
    public void setSelectedCourse(Course c) {
        this.courseId = c.getCourseId();
        this.courseName = c.getCourseName();
    }

    // UPDATE METHOD
    public void updateCourse() {
        if (courseId == null) {
            System.out.println("ERROR: courseId is NULL");
            return;
        }
        abl.updateCourse(courseName, courseId);
    }

    // GETTERS & SETTERS
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
}
