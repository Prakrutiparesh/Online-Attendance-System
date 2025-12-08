/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.AttendanceSummary;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import Entity.Users;
import jakarta.ejb.Local;
import java.sql.Date;
import java.util.Collection;

/**
 *
 * @author PRAKRUTI
 */
@Local
public interface AdminBeanLocal {

    //Course Method
    public void addCourse(String courseName);

    public void updateCourse(String courseName, Integer coursId);

    public void deleteCourse(Integer courseId);

    public Course findCourseById(Integer courseId);

    public Collection<Course> getAllCourse();

    public Course findCourseByName(String courseName);

    //Semester Method
    public void addSemester(String semName, Integer courseId);

    public void updateSemester(Integer semId, String semName, Integer courseId);

    public void deleteSemester(Integer semId, Integer courseId);

    public Collection<Semester> getAllSemesters();

    public Collection<Semester> getSemestersByCourse(Integer courseId);

//Subject Method
    public void addSubject(String subName, Integer courseId, Integer semId);

    public void updateSubject(Integer subId, String subName, Integer courseId, Integer semId);

    public void deleteSubject(Integer subId, Integer courseId, Integer semId);

    public Collection<Subject> getAllSubjects();

    public Collection<Subject> getSubjectsByCourseandSemester(Integer courseId, Integer semId);

    //Division Method
    public void addDivision(String divName, Integer semId, Integer courseId);

    public void updateDivision(Integer divId, String divName, Integer semId, Integer courseId);

    public void deleteDivision(Integer divId, Integer semId, Integer courseId);

    public Collection<Division> getAllDivision();

    public Collection<Division> getDivisionsByCourseAndSemester(Integer semId, Integer courseId);

    public Collection<Division> getDivisionsByCourse(Integer courseId);

    public Collection<Division> getDivisionsBySemester(Integer semId);

    //Student Method
    public void addStudent(String studentName, Integer rollNo, Integer courseId, Integer semId, Integer divId);

    public void updateStudent(Integer studId, String studName, Integer rollNo, Integer courseId, Integer semId, Integer divId);

    public void deleteStudent(Integer studId, Integer selectedCourseId, Integer selectedSemId, Integer selectedStudentDivId);

    public Student findStudentById(Integer studId);

    public Collection<Student> getAllStudents();

    public Collection<Student> getStudentsByCourse(Integer courseId);

    public Collection<Student> getStudentsBySem(Integer semId);

    public Collection<Student> getStudentsByCourseSemDiv(Integer courseId, Integer semId, Integer divId);

    //Attendance Summary Method
    public void updateSummary(Integer studId, Integer subId);

    public AttendanceSummary findSummaryByStudent(Integer studId);

    public AttendanceSummary findSummaryByStudentAndSubject(Integer studId, Integer subId);

    public Collection<AttendanceSummary> getAllSummaries();

    public Collection<AttendanceSummary> getSummaryByCourse(Integer courseId);

    public Collection<AttendanceSummary> getSummaryBySemester(Integer semId);

//Register User Method
    public void registerUser(String username, String password, String name, java.util.Date dob, String mobile, String email, Integer groupId);

    public Collection<Users> getAllUsers();

    public String saySecureHello();

}
