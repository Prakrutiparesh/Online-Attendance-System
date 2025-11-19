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
@Table(name = "subject")
@NamedQueries({
    @NamedQuery(name = "Subject.findAll", query = "SELECT s FROM Subject s"),
    @NamedQuery(name = "Subject.findBySubId", query = "SELECT s FROM Subject s WHERE s.subId = :subId"),
    @NamedQuery(name = "Subject.findBySubName", query = "SELECT s FROM Subject s WHERE s.subName = :subName")})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sub_id")
    private Integer subId;
    @Column(name = "sub_name")
    private String subName;

    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne
    private Course course;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subject")
    private Collection<Attendance> attendanceCollection;

    @JoinColumn(name = "sem_id", referencedColumnName = "sem_id")
    @ManyToOne
    private Semester semester;

    public Subject() {
    }

    public Subject(Integer subId) {
        this.subId = subId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Collection<Attendance> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<Attendance> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subId != null ? subId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.subId == null && other.subId != null) || (this.subId != null && !this.subId.equals(other.subId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Subject[ subId=" + subId + " ]";
    }

}
