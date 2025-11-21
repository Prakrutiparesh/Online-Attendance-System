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
@Table(name = "student")
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findByStudId", query = "SELECT s FROM Student s WHERE s.studId = :studId"),
    @NamedQuery(name = "Student.findByRollNo", query = "SELECT s FROM Student s WHERE s.rollNo = :rollNo"),})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "stud_id")
    private Integer studId;
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "roll_no")
    private Integer rollNo;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;
    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "sem_id", referencedColumnName = "sem_id")
    private Semester semester;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "div_id", referencedColumnName = "div_id")
    private Division division;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<Attendance> attendanceCollection;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<AttendanceSummary> attendanceSummaryCollection;

    public Student() {
    }

    public Student(Integer studId) {
        this.studId = studId;
    }

    public Integer getStudId() {
        return studId;
    }

    public void setStudId(Integer studId) {
        this.studId = studId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Collection<Attendance> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<Attendance> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
    }

    public Collection<AttendanceSummary> getAttendanceSummaryCollection() {
        return attendanceSummaryCollection;
    }

    public void setAttendanceSummaryCollection(Collection<AttendanceSummary> attendanceSummaryCollection) {
        this.attendanceSummaryCollection = attendanceSummaryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studId != null ? studId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.studId == null && other.studId != null) || (this.studId != null && !this.studId.equals(other.studId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Student[ studId=" + studId + " ]";
    }

}
