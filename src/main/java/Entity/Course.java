/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author PRAKRUTI
 */
@Entity
@Table(name = "course")
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findByCourseId", query = "SELECT c FROM Course c WHERE c.courseId = :courseId"),
    @NamedQuery(name = "Course.findByCourseName", query = "SELECT c FROM Course c WHERE c.courseName = :courseName")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "course_id")
    private Integer courseId;
    @Basic(optional = false)
    @Column(name = "course_name")
    private String courseName;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Semester> semesterCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Division> divisionCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Subject> subjectCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Student> studentCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Attendance> attendanceCollection;

    public Course() {
    }

    public Course(Integer courseId) {
        this.courseId = courseId;
    }

    public Course(Integer courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
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

    public Collection<Semester> getSemesterCollection() {
        return semesterCollection;
    }

    public void setSemesterCollection(Collection<Semester> semesterCollection) {
        this.semesterCollection = semesterCollection;
    }

    public Collection<Division> getDivisionCollection() {
        return divisionCollection;
    }

    public void setDivisionCollection(Collection<Division> divisionCollection) {
        this.divisionCollection = divisionCollection;
    }

    public Collection<Subject> getSubjectCollection() {
        return subjectCollection;
    }

    public void setSubjectCollection(Collection<Subject> subjectCollection) {
        this.subjectCollection = subjectCollection;
    }

    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    public Collection<Attendance> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<Attendance> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseId != null ? courseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.courseId == null && other.courseId != null) || (this.courseId != null && !this.courseId.equals(other.courseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Course[ courseId=" + courseId + " ]";
    }

}
