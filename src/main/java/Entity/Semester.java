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
@Table(name = "semester")
@NamedQueries({
    @NamedQuery(name = "Semester.findAll", query = "SELECT s FROM Semester s"),
    @NamedQuery(name = "Semester.findBySemId", query = "SELECT s FROM Semester s WHERE s.semId = :semId"),
    @NamedQuery(name = "Semester.findBySemName", query = "SELECT s FROM Semester s WHERE s.semName = :semName")})
public class Semester implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sem_id")
    private Integer semId;
    @Column(name = "sem_name")
    private String semName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Collection<Division> divisionCollection;

    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne
    private Course course;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Collection<Student> studentCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Collection<Attendance> attendanceCollection;

    public Semester() {
    }

    public Semester(Integer semId) {
        this.semId = semId;
    }

    public Integer getSemId() {
        return semId;
    }

    public void setSemId(Integer semId) {
        this.semId = semId;
    }

    public String getSemName() {
        return semName;
    }

    public void setSemName(String semName) {
        this.semName = semName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Collection<Division> getDivisionCollection() {
        return divisionCollection;
    }

    public void setDivisionCollection(Collection<Division> divisionCollection) {
        this.divisionCollection = divisionCollection;
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
        hash += (semId != null ? semId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Semester)) {
            return false;
        }
        Semester other = (Semester) object;
        if ((this.semId == null && other.semId != null) || (this.semId != null && !this.semId.equals(other.semId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Semester[ semId=" + semId + " ]";
    }

}
