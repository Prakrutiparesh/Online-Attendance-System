/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Attendance;
import Entity.AttendanceSummary;
import Entity.Course;
import Entity.Division;
import Entity.Groupmaster;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import Entity.Users;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author PRAKRUTI
 */
@Stateless
@DeclareRoles({"Admin", "Faculty"})
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "OAS")
    EntityManager em;
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    //Course Logic
    @Override
    public void addCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        Course c = new Course();
        c.setCourseName(courseName.trim());
        em.persist(c);
    }

    @Override
    public void updateCourse(String courseName, Integer courseId) {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new IllegalArgumentException("Invalid Course ID: " + courseId);
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        c.setCourseName(courseName.trim());
        em.merge(c);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new IllegalArgumentException("Invalid Course ID: " + courseId);
        }
        em.remove(c);
    }

    @Override
    public Course findCourseById(Integer courseId) {
        try {
            return em.createNamedQuery("Course.findByCourseId", Course.class)
                    .setParameter("courseId", courseId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Course not found for ID: " + courseId);
        }
    }

    @Override
    public Collection<Course> getAllCourse() {
        try {
            return em.createNamedQuery("Course.findAll", Course.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Course findCourseByName(String courseName) {
        try {
            return em.createNamedQuery("Course.findByCourseName", Course.class)
                    .setParameter("courseName", courseName)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("No course found with name: " + courseName);
        }
    }

//Semester Logic
    @Override
    public void addSemester(String semName, Integer courseId) {
        try {
            Course c = em.find(Course.class, courseId);
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }

            Semester s = new Semester();
            s.setSemName(semName);
            s.setCourse(c);

            c.getSemesterCollection().add(s);

            em.persist(s);
            em.merge(c);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Course ID: " + courseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSemester(Integer semId, String semName, Integer courseId) {
        try {
            Semester s = em.find(Semester.class, semId);
            if (s == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }

            s.setSemName(semName);

            if (courseId != null) {
                Course c = em.find(Course.class, courseId);
                if (c == null) {
                    throw new IllegalArgumentException("Invalid CourseID: " + courseId);
                }
                s.setCourse(c);
            }

            em.merge(s);
            em.flush();
            em.clear();

            System.out.println("Semester updated in DB: " + semId + " -> " + semName);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Semester ID: " + semId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSemester(Integer semId, Integer courseId) {
        try {
            // Direct find and remove
            Semester s = em.find(Semester.class, semId);
            if (s == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }

            em.remove(s);
            em.flush();
            em.clear();
            System.out.println("Semester " + semId + " deleted successfully");

        } catch (Exception e) {
            System.out.println("Delete error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override

    public Collection<Semester> getAllSemesters() {
        try {
            return em.createNamedQuery("Semester.findAll", Semester.class)
                    .getResultList();
        } catch (NoResultException e) {
//            throw  new NoResultException("Error");
            System.out.println("No semesters found.");
            return java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public Collection<Semester> getSemestersByCourse(Integer courseId) {
        try {
            // DIRECT QUERY - No entity caching
            String jpql = "SELECT s FROM Semester s WHERE s.course.courseId = :courseId ORDER BY s.semId";
            TypedQuery<Semester> query = em.createQuery(jpql, Semester.class);
            query.setParameter("courseId", courseId);

            List<Semester> result = query.getResultList();
            System.out.println("DB Query returned: " + result.size() + " semesters for course: " + courseId);

            return result;

        } catch (Exception e) {
            System.out.println("Error in getSemestersByCourse: " + e.getMessage());
            return new ArrayList<>();
        }
    }
//Subject Logic

    @Override
    public void addSubject(String subName, Integer courseId, Integer semId) {
        try {
            Course c = em.find(Course.class, courseId);
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }

            Semester sem = em.find(Semester.class, semId);
            if (sem == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }

            Subject s = new Subject();
            s.setSubName(subName);
            s.setCourse(c);
            s.setSemester(sem);

            Collection<Subject> subjects = c.getSubjectCollection();
            subjects.add(s);
            c.setSubjectCollection(subjects);

            Collection<Subject> semSubjects = sem.getSubjectCollection();
            semSubjects.add(s);
            sem.setSubjectCollection(semSubjects);

            em.persist(s);
            em.merge(c);
            em.merge(sem);

            System.out.println("Subject added successfully: " + subName);

        } catch (IllegalArgumentException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in addSubject(): " + e.getMessage());
            throw new RuntimeException("Error adding subject: " + e.getMessage());
        }
    }

    @Override
    public void updateSubject(Integer subId, String subName,
            Integer courseId
    ) {
        try {
            Subject s = em.find(Subject.class, subId);
            if (s == null) {
                throw new IllegalArgumentException("Subject not found with ID: " + subId);
            }

            s.setSubName(subName);
            if (courseId != null) {
                Course c = em.find(Course.class, courseId);
                if (c == null) {
                    throw new IllegalArgumentException("Invalid Course ID: " + courseId);
                }
                s.setCourse(c);
            }

            em.merge(s);
            System.out.println("Subject updated successfully: " + subName);

        } catch (IllegalArgumentException e) {
            System.err.println("Error updating subject: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in updateSubject(): " + e.getMessage());
            throw new RuntimeException("Error updating subject: " + e.getMessage());
        }
    }

    @Override
    public void deleteSubject(Integer subId, Integer courseId
    ) {
        try {
            Course c = em.find(Course.class, courseId);
            Subject s = em.find(Subject.class, subId);

            if (c == null || s == null) {
                throw new IllegalArgumentException("Invalid Course or Subject ID");
            }

            Collection<Subject> subjects = c.getSubjectCollection();
            if (subjects.contains(s)) {
                subjects.remove(s);
                c.setSubjectCollection(subjects);
                em.remove(em.merge(s));
                System.out.println("Subject deleted successfully: ID " + subId);
            } else {
                throw new IllegalArgumentException("Subject does not belong to this course");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in deleteSubject(): " + e.getMessage());
            throw new RuntimeException("Error deleting subject: " + e.getMessage());
        }
    }

    @Override
    public Collection<Subject> getAllSubjects() {
        try {
            return em.createNamedQuery("Subject.findAll", Subject.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching all subjects: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Subject> getSubjectsByCourse(Integer courseId
    ) {
        try {
            Course c = em.find(Course.class, courseId);
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }
            return c.getSubjectCollection();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error fetching subjects by course: " + e.getMessage());
            return Collections.emptyList();
        }
    }
//Division Logic

    @Override
    public void addDivision(String divName, Integer semId,
            Integer courseId
    ) {
        Course c = em.find(Course.class, courseId);
        Semester s = em.find(Semester.class, semId);

        if (c == null) {
            throw new IllegalArgumentException("Invalid Course ID: " + courseId);
        }
        if (s == null) {
            throw new IllegalArgumentException("Invalid Semester ID: " + semId);
        }

        Division d = new Division();
        d.setDivName(divName);
        d.setCourse(c);
        d.setSemester(s);
        em.persist(d);
    }

    @Override
    public void updateDivision(Integer divId, String divName,
            Integer semId, Integer courseId
    ) {
        Division d = em.find(Division.class, divId);
        if (d == null) {
            throw new IllegalArgumentException("Invalid Division ID: " + divId);
        }

        d.setDivName(divName);

        if (semId != null) {
            Semester s = em.find(Semester.class, semId);
            if (s == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }
            d.setSemester(s);
        }

        if (courseId != null) {
            Course c = em.find(Course.class, courseId);
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }
            d.setCourse(c);
        }

        em.merge(d);
    }

    @Override
    public void deleteDivision(Integer divId, Integer semId,
            Integer courseId
    ) {
        Division d = em.find(Division.class, divId);
        if (d == null) {
            throw new IllegalArgumentException("Invalid Division ID: " + divId);
        }

        // Validate course
        if (courseId != null) {
            Course c = d.getCourse();
            if (c == null || c.getCourseId() == null || !c.getCourseId().equals(courseId)) {
                throw new IllegalArgumentException("Division " + divId + " does not belong to Course ID: " + courseId);
            }
        }

        // Validate semester
        if (semId != null) {
            Semester s = d.getSemester();
            if (s == null || s.getSemId() == null || !s.getSemId().equals(semId)) {
                throw new IllegalArgumentException("Division " + divId + " does not belong to Semester ID: " + semId);
            }
        }

        // Maintain bidirectional relation cleanup
        Course parentCourse = d.getCourse();
        if (parentCourse != null && parentCourse.getDivisionCollection() != null) {
            parentCourse.getDivisionCollection().remove(d);
        }
        Semester parentSem = d.getSemester();
        if (parentSem != null && parentSem.getDivisionCollection() != null) {
            parentSem.getDivisionCollection().remove(d);
        }

        em.remove(d);
    }

    @Override
    public Collection<Division> getDivisionsByCourse(Integer courseId
    ) {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new IllegalArgumentException("Invalid Course ID: " + courseId);
        }
        return c.getDivisionCollection();
    }

    @Override
    public Collection<Division> getDivisionsBySemester(Integer semId
    ) {
        Semester s = em.find(Semester.class, semId);
        if (s == null) {
            throw new IllegalArgumentException("Invalid Semester ID: " + semId);
        }
        return s.getDivisionCollection();
    }
// Student Logic

    @Override
    public void addStudent(Integer userId, Integer rollNo,
            Integer courseId, Integer semId,
            Integer divId
    ) {
        try {
            Users u = em.find(Users.class, userId);
            Course c = em.find(Course.class, courseId);
            Semester s = em.find(Semester.class, semId);
            Division d = em.find(Division.class, divId);

            if (u == null) {
                throw new IllegalArgumentException("Invalid User ID: " + userId);
            }
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }
            if (s == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }
            if (d == null) {
                throw new IllegalArgumentException("Invalid Division ID: " + divId);
            }

            Student st = new Student();
            st.setUsers(u);
            st.setRollNo(rollNo);
            st.setCourse(c);
            st.setSemester(s);
            st.setDivision(d);

            em.persist(st);
            System.out.println("Student added successfully: " + u.getUserId());

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;  // Pass to REST layer
        } catch (Exception e) {
            System.err.println("Unexpected error in addStudent(): " + e.getMessage());
            throw new RuntimeException("Error adding student: " + e.getMessage());
        }
    }

    @Override
    public void updateStudent(Integer studId, Integer rollNo,
            Integer semId, Integer divId
    ) {
        try {
            Student st = em.find(Student.class, studId);
            if (st == null) {
                throw new IllegalArgumentException("Invalid Student ID: " + studId);
            }

            if (rollNo != null) {
                st.setRollNo(rollNo);
            }

            if (semId != null) {
                Semester s = em.find(Semester.class, semId);
                if (s == null) {
                    throw new IllegalArgumentException("Invalid Semester ID: " + semId);
                }
                st.setSemester(s);
            }

            if (divId != null) {
                Division d = em.find(Division.class, divId);
                if (d == null) {
                    throw new IllegalArgumentException("Invalid Division ID: " + divId);
                }
                st.setDivision(d);
            }

            em.merge(st);
            System.out.println("Student updated successfully: " + studId);

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e; // Pass error to REST layer
        } catch (Exception e) {
            System.err.println("Unexpected error in updateStudent(): " + e.getMessage());
            throw new RuntimeException("Error updating student: " + e.getMessage());
        }
    }

    @Override
    public void deleteStudent(Integer studId
    ) {
        try {
            Student st = em.find(Student.class, studId);
            if (st == null) {
                throw new IllegalArgumentException("Invalid Student ID: " + studId);
            }

            // Remove from related collections (optional but safe practice)
            Course c = st.getCourse();
            if (c != null && c.getStudentCollection() != null) {
                c.getStudentCollection().remove(st);
            }

            Semester s = st.getSemester();
            if (s != null && s.getStudentCollection() != null) {
                s.getStudentCollection().remove(st);
            }

            Division d = st.getDivision();
            if (d != null && d.getStudentCollection() != null) {
                d.getStudentCollection().remove(st);
            }

            em.remove(st);
            System.out.println("Student deleted successfully: " + studId);

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;  // Pass to REST
        } catch (Exception e) {
            System.err.println("Unexpected error in deleteStudent(): " + e.getMessage());
            throw new RuntimeException("Error deleting student: " + e.getMessage());
        }
    }

    @Override
    public Collection<Student> getAllStudents() {
        try {
            return em.createNamedQuery("Student.findAll", Student.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public Student findStudentById(Integer studId
    ) {
        try {
            return em.find(Student.class, studId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<Student> getStudentsByCourse(Integer courseId
    ) {
        try {
            Course c = em.find(Course.class, courseId);
            if (c != null && c.getStudentCollection() != null) {
                return c.getStudentCollection();
            }
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Student> getStudentsBySem(Integer semId
    ) {
        try {
            Semester s = em.find(Semester.class, semId);
            if (s != null && s.getStudentCollection() != null) {
                return s.getStudentCollection();
            }
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
//Attendance Summary Logic

    @Override
    public void updateSummary(Integer studId
    ) {
        try {
            Student st = em.find(Student.class, studId);
            if (st == null) {
                throw new IllegalArgumentException("Invalid Student ID: " + studId);
            }

            // Fetch all attendance records for this student
            TypedQuery<Attendance> query = em.createQuery(
                    "SELECT a FROM Attendance a WHERE a.student.studId = :studId", Attendance.class);
            query.setParameter("studId", studId);
            List<Attendance> attendanceList = query.getResultList();

            if (attendanceList == null || attendanceList.isEmpty()) {
                throw new IllegalArgumentException("No attendance records found for student ID: " + studId);
            }

            int totalPresent = 0;
            int totalAbsent = 0;

            for (Attendance a : attendanceList) {
                if ("Present".equalsIgnoreCase(a.getStatus())) {
                    totalPresent++;
                } else if ("Absent".equalsIgnoreCase(a.getStatus())) {
                    totalAbsent++;
                }
            }

            // Count total students (in same course & semester)
            TypedQuery<Long> countQuery = em.createQuery(
                    "SELECT COUNT(s) FROM Student s WHERE s.course.courseId = :cid AND s.semester.semId = :sid", Long.class);
            countQuery.setParameter("cid", st.getCourse().getCourseId());
            countQuery.setParameter("sid", st.getSemester().getSemId());
            Long totalStudentCount = countQuery.getSingleResult();

            // Calculate average attendance for this student's course/semester
            TypedQuery<Double> avgQuery = em.createQuery(
                    "SELECT AVG(s.totalPresent * 100.0 / (s.totalPresent + s.totalAbsent)) "
                    + "FROM AttendanceSummary s WHERE s.student.course.courseId = :cid AND s.student.semester.semId = :sid", Double.class);
            avgQuery.setParameter("cid", st.getCourse().getCourseId());
            avgQuery.setParameter("sid", st.getSemester().getSemId());
            Double avgAttendance = avgQuery.getSingleResult();
            if (avgAttendance == null) {
                avgAttendance = 0.0;
            }

            // Check if summary already exists
            TypedQuery<AttendanceSummary> existingQuery = em.createQuery(
                    "SELECT s FROM AttendanceSummary s WHERE s.student.studId = :studId", AttendanceSummary.class);
            existingQuery.setParameter("studId", studId);
            List<AttendanceSummary> existingList = existingQuery.getResultList();

            AttendanceSummary summary;
            if (existingList.isEmpty()) {
                summary = new AttendanceSummary();
                summary.setStudent(st);
            } else {
                summary = existingList.get(0);
            }

            summary.setTotalPresent(totalPresent);
            summary.setTotalAbsent(totalAbsent);
            summary.setTotalStudent(totalStudentCount.intValue());
            summary.setAvgAttendance(avgAttendance.floatValue());

            em.merge(summary);
            System.out.println("Attendance summary updated successfully for Student ID: " + studId);

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error in updateSummary(): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in updateSummary(): " + e.getMessage());
            throw new RuntimeException("Error updating summary: " + e.getMessage());
        }
    }

    @Override
    public AttendanceSummary findSummaryByStudent(Integer studId
    ) {
        try {
            TypedQuery<AttendanceSummary> query = em.createQuery(
                    "SELECT s FROM AttendanceSummary s WHERE s.student.studId = :studId", AttendanceSummary.class);
            query.setParameter("studId", studId);
            List<AttendanceSummary> list = query.getResultList();

            if (list.isEmpty()) {
                throw new IllegalArgumentException("No summary found for Student ID: " + studId);
            }

            return list.get(0);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error in findSummaryByStudent(): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in findSummaryByStudent(): " + e.getMessage());
            throw new RuntimeException("Error finding summary by student: " + e.getMessage());
        }
    }

    @Override
    public Collection<AttendanceSummary> getAllSummaries() {
        try {
            return em.createQuery("SELECT s FROM AttendanceSummary s", AttendanceSummary.class)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Unexpected error in getAllSummaries(): " + e.getMessage());
            throw new RuntimeException("Error fetching all summaries: " + e.getMessage());
        }
    }

    @Override
    public Collection<AttendanceSummary> getSummaryByCourse(Integer courseId
    ) {
        try {
            Course c = em.find(Course.class, courseId);
            if (c == null) {
                throw new IllegalArgumentException("Invalid Course ID: " + courseId);
            }

            TypedQuery<AttendanceSummary> query = em.createQuery(
                    "SELECT s FROM AttendanceSummary s WHERE s.student.course.courseId = :courseId",
                    AttendanceSummary.class);
            query.setParameter("courseId", courseId);
            List<AttendanceSummary> list = query.getResultList();

            if (list.isEmpty()) {
                throw new IllegalArgumentException("No summaries found for Course ID: " + courseId);
            }

            return list;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error in getSummaryByCourse(): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in getSummaryByCourse(): " + e.getMessage());
            throw new RuntimeException("Error fetching summary by course: " + e.getMessage());
        }
    }

    @Override
    public Collection<AttendanceSummary> getSummaryBySemester(Integer semId
    ) {
        try {
            Semester s = em.find(Semester.class, semId);
            if (s == null) {
                throw new IllegalArgumentException("Invalid Semester ID: " + semId);
            }

            TypedQuery<AttendanceSummary> query = em.createQuery(
                    "SELECT s FROM AttendanceSummary s WHERE s.student.semester.semId = :semId",
                    AttendanceSummary.class);
            query.setParameter("semId", semId);
            List<AttendanceSummary> list = query.getResultList();

            if (list.isEmpty()) {
                throw new IllegalArgumentException("No summaries found for Semester ID: " + semId);
            }

            return list;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error in getSummaryBySemester(): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in getSummaryBySemester(): " + e.getMessage());
            throw new RuntimeException("Error fetching summary by semester: " + e.getMessage());
        }
    }

//Register User Logic
    @Override
    public void registerUser(String username, String password,
            String name, Date dob,
            String mobile, String email,
            Integer groupId
    ) {
        try {
            Groupmaster g = em.find(Groupmaster.class, groupId);

            if (g != null) {
                Users u = new Users();
                u.setUsername(username);
                u.setPassword(passwordHash.generate(password.toCharArray())); // No need to initialize manually
                u.setName(name);
                u.setDob(dob);
                u.setMobile(mobile);
                u.setEmail(email);
                u.setGroupmaster(g);

                g.getUsersCollection().add(u);
                em.persist(u);
                em.merge(g);

                System.out.println("User registered successfully!");
            } else {
                throw new IllegalArgumentException("Invalid Group ID: " + groupId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while registering user: " + e.getMessage());
        }
    }

    @RolesAllowed("Admin")
//@PermitAll  
    //@DenyAll   
    public String saySecureHello() {
        return "Secure Hello from Secure Bean";
    }

}
