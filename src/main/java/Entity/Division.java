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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "division")
@NamedQueries({
    @NamedQuery(name = "Division.findAll", query = "SELECT d FROM Division d"),
    @NamedQuery(name = "Division.findByDivId", query = "SELECT d FROM Division d WHERE d.divId = :divId"),
    @NamedQuery(name = "Division.findByDivName", query = "SELECT d FROM Division d WHERE d.divName = :divName")})
public class Division implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "div_id")
    private Integer divId;
    @Column(name = "div_name")
    private String divName;

    @JsonbTransient
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne
    private Course course;
    @JsonbTransient
    @JoinColumn(name = "sem_id", referencedColumnName = "sem_id")
    @ManyToOne
    private Semester semester;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "division")
    private Collection<Student> studentCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "division")
    private Collection<Attendance> attendanceCollection;

    public Division() {
    }

    public Division(Integer divId) {
        this.divId = divId;
    }

    public Integer getDivId() {
        return divId;
    }

    public void setDivId(Integer divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
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
        hash += (divId != null ? divId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Division)) {
            return false;
        }
        Division other = (Division) object;
        if ((this.divId == null && other.divId != null) || (this.divId != null && !this.divId.equals(other.divId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Division[ divId=" + divId + " ]";
    }

}
