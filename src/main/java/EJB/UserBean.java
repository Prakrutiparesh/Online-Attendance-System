/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Attendance;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import Entity.Users;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author PRAKRUTI
 */
@Stateless
public class UserBean implements UserBeanLocal {

    @PersistenceContext(unitName = "OAS")
    EntityManager em;

//Attendance Logic
    @Override
    public void markAttendance(Integer studId, Integer userId, Integer courseId, Integer subId, Integer semId, Integer divId, Date date, String status) {
        try {
            Student s = em.find(Student.class, studId);
            Users u = em.find(Users.class, userId);
            Course c = em.find(Course.class, courseId);
            Subject sub = em.find(Subject.class, subId);
            Semester sem = em.find(Semester.class, semId);
            Division div = em.find(Division.class, divId);

            if (s == null || u == null || c == null || sub == null || sem == null || div == null) {
                throw new IllegalArgumentException("Invalid Foreign Key Reference Found. Please check all IDs.");
            }

            Long count = em.createQuery(
                    "SELECT COUNT(a) FROM Attendance a WHERE "
                    + "a.student.studId = :sid AND "
                    + "a.course.courseId = :cid AND "
                    + "a.subject.subId = :subid AND "
                    + "a.semester.semId = :semid AND "
                    + "a.division.divId = :divid AND "
                    + "a.attendanceDate = :date",
                    Long.class
            )
                    .setParameter("sid", studId)
                    .setParameter("cid", courseId)
                    .setParameter("subid", subId)
                    .setParameter("semid", semId)
                    .setParameter("divid", divId)
                    .setParameter("date", date)
                    .getSingleResult();

            if (count > 0) {
                throw new IllegalArgumentException("Attendance already marked for this student in this subject today!");
            }

            Attendance att = new Attendance();
            att.setStudent(s);
            att.setUsers(u);
            att.setCourse(c);
            att.setSubject(sub);
            att.setSemester(sem);
            att.setDivision(div);
            att.setAttendanceDate(date);
            att.setStatus(status);

            em.persist(att);
            System.out.println("Attendance marked successfully for Student ID: " + studId);

        } catch (IllegalArgumentException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error while marking attendance: " + e.getMessage());
        }
    }

    @Override
    public void updateAttendance(Integer attendanceId, String status) {
        try {
            Attendance a = em.find(Attendance.class, attendanceId);
            if (a == null) {
                throw new IllegalArgumentException("Invalid Attendance ID: " + attendanceId);
            }

            a.setStatus(status);
            em.merge(a);

            System.out.println("Attendance updated successfully for ID: " + attendanceId);

        } catch (IllegalArgumentException e) {
            System.out.println("Error updating attendance: " + e.getMessage());

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error while Update  attendance: " + e.getMessage());

        }
    }

    @Override
    public void deleteAttendance(Integer attendanceId) {
        try {
            Attendance a = em.find(Attendance.class, attendanceId);
            if (a == null) {
                throw new IllegalArgumentException("Invalid Attendance ID: " + attendanceId);
            }

            em.remove(a);

            System.out.println("Attendance deleted successfully for ID: " + attendanceId);

        } catch (IllegalArgumentException e) {

            System.out.println("Error deleting attendance: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Attendance> getAttendanceByStudent(Integer studId) {
        try {
            return em.createQuery("SELECT a FROM Attendance a WHERE a.student.studId = :sid ORDER BY a.attendanceDate DESC", Attendance.class)
                    .setParameter("sid", studId)
                    .getResultList();

        } catch (NoResultException e) {
            System.out.println("No attendance found for student ID: " + studId);
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Attendance> getAttendanceByDate(Date date) {
        try {
            return em.createQuery("SELECT a FROM Attendance a WHERE a.attendanceDate = :date ORDER BY a.student.studId", Attendance.class)
                    .setParameter("date", date)
                    .getResultList();

        } catch (NoResultException e) {
            System.out.println("No attendance found for date: " + date);
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
