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
import java.io.Serializable;

/**
 *
 * @author PRAKRUTI
 */
@Entity
@Table(name = "attendance_summary")
@NamedQueries({
    @NamedQuery(name = "AttendanceSummary.findAll", query = "SELECT a FROM AttendanceSummary a"),
    @NamedQuery(name = "AttendanceSummary.findByAttendanceSummaryId", query = "SELECT a FROM AttendanceSummary a WHERE a.attendanceSummaryId = :attendanceSummaryId"),

    @NamedQuery(name = "AttendanceSummary.findByTotalPresent", query = "SELECT a FROM AttendanceSummary a WHERE a.totalPresent = :totalPresent"),
    @NamedQuery(name = "AttendanceSummary.findByTotalAbsent", query = "SELECT a FROM AttendanceSummary a WHERE a.totalAbsent = :totalAbsent"),
    @NamedQuery(name = "AttendanceSummary.findByTotalStudent", query = "SELECT a FROM AttendanceSummary a WHERE a.totalStudent = :totalStudent"),
    @NamedQuery(name = "AttendanceSummary.findByAvgAttendance", query = "SELECT a FROM AttendanceSummary a WHERE a.avgAttendance = :avgAttendance")})
public class AttendanceSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attendance_summary_id")
    private Integer attendanceSummaryId;

    @Column(name = "total_present")
    private Integer totalPresent;
    @Column(name = "total_absent")
    private Integer totalAbsent;
    @Column(name = "total_student")
    private Integer totalStudent;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avg_attendance")
    private Float avgAttendance;

    @ManyToOne
    @JoinColumn(name = "stud_id", referencedColumnName = "stud_id")
    private Student student;

    public AttendanceSummary() {
    }

    public AttendanceSummary(Integer attendanceSummaryId) {
        this.attendanceSummaryId = attendanceSummaryId;
    }

    public Integer getAttendanceSummaryId() {
        return attendanceSummaryId;
    }

    public void setAttendanceSummaryId(Integer attendanceSummaryId) {
        this.attendanceSummaryId = attendanceSummaryId;
    }

    public Integer getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(Integer totalPresent) {
        this.totalPresent = totalPresent;
    }

    public Integer getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(Integer totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public Integer getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(Integer totalStudent) {
        this.totalStudent = totalStudent;
    }

    public Float getAvgAttendance() {
        return avgAttendance;
    }

    public void setAvgAttendance(Float avgAttendance) {
        this.avgAttendance = avgAttendance;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attendanceSummaryId != null ? attendanceSummaryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttendanceSummary)) {
            return false;
        }
        AttendanceSummary other = (AttendanceSummary) object;
        if ((this.attendanceSummaryId == null && other.attendanceSummaryId != null) || (this.attendanceSummaryId != null && !this.attendanceSummaryId.equals(other.attendanceSummaryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.AttendanceSummary[ attendanceSummaryId=" + attendanceSummaryId + " ]";
    }

}
