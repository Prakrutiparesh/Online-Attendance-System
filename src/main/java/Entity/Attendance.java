/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author PRAKRUTI
 */
@Entity
@Table(name = "attendance")
@NamedQueries({
    @NamedQuery(name = "Attendance.findAll", query = "SELECT a FROM Attendance a"),
    @NamedQuery(name = "Attendance.findByAttendanceId", query = "SELECT a FROM Attendance a WHERE a.attendanceId = :attendanceId"),

    @NamedQuery(name = "Attendance.findByAttendanceDate", query = "SELECT a FROM Attendance a WHERE a.attendanceDate = :attendanceDate"),
    @NamedQuery(name = "Attendance.findByStatus", query = "SELECT a FROM Attendance a WHERE a.status = :status")})
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @Column(name = "attendance_date")
    @Temporal(TemporalType.DATE)
    private Date attendanceDate;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "stud_id", referencedColumnName = "stud_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "sub_id", referencedColumnName = "sub_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "sem_id", referencedColumnName = "sem_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "div_id", referencedColumnName = "div_id")
    private Division division;

    public Attendance() {
    }

    public Attendance(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attendanceId != null ? attendanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attendance)) {
            return false;
        }
        Attendance other = (Attendance) object;
        if ((this.attendanceId == null && other.attendanceId != null) || (this.attendanceId != null && !this.attendanceId.equals(other.attendanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Attendance[ attendanceId=" + attendanceId + " ]";
    }

}
