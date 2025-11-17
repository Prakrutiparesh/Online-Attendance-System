/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package REST;

import EJB.AdminBeanLocal;
import Entity.AttendanceSummary;
import Entity.Course;
import Entity.Division;
import Entity.Semester;
import Entity.Student;
import Entity.Subject;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author PRAKRUTI
 */
@DeclareRoles("Admin")
@Path("Admin")
public class AdminRest {

    //Course REST
    @EJB
    AdminBeanLocal abl;
    @Context
    private UriInfo context;

    @POST
    @Path("addcourse/{coursename}")
    public String addCourse(@PathParam("coursename") String courseName) {
        try {
            abl.addCourse(courseName);
            return "{\"message\": \"Course added successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("allcourse")
    public Collection<Course> getAllCourse() {
        try {
            return abl.getAllCourse();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList(); // safe fallback
        }
    }

    @GET
    @Path("findcourse/{courseid}")
    public Course getCourse(@PathParam("courseid") Integer courseId) {
        try {
            Course c = abl.findCourseById(courseId);
            if (c == null) {
                System.out.println("Course not found with ID: " + courseId);
            }
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DELETE
    @Path("deletecourse/{courseid}")
    public String deleteCourse(@PathParam("courseid") Integer courseId) {
        try {
            abl.deleteCourse(courseId);
            return "{\"message\": \"Course deleted successfully: " + courseId + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PUT
    @Path("updatecourse/{courseid}/{coursename}")
    public String updateCourse(@PathParam("courseid") Integer courseId, @PathParam("coursename") String courseName) {
        try {
            abl.updateCourse(courseName, courseId);
            return "{\"message\": \"Updated Course: " + courseName + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("findcoursename/{coursename}")
    public Course findCouseByName(@PathParam("coursename") String courseName) {
        try {
            return abl.findCourseByName(courseName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Semester Rest
    @POST
    @Path("addsemester/{semname}/{courseid}")
    public String addSemester(@PathParam("semname") String semName,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.addSemester(semName, courseId);
            return "{\"message\": \"Semester added successfully: " + semName + " (Course ID: " + courseId + ")\"}";
        } catch (IllegalArgumentException e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PUT
    @Path("updatesemester/{semid}/{semname}/{courseid}")
    public String updateSemester(@PathParam("semid") Integer semId,
            @PathParam("semname") String semName,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.updateSemester(semId, semName, courseId);
            return "{\"message\": \"Semester updated successfully: ID=" + semId + ", Name=" + semName + ", Course ID=" + courseId + "\"}";
        } catch (IllegalArgumentException e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to update semester: " + e.getMessage() + "\"}";
        }
    }

    @DELETE
    @Path("deletesemester/{semid}/{courseid}")
    public String deleteSemester(@PathParam("semid") Integer semId,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.deleteSemester(semId, courseId);
            return "{\"message\": \"Semester deleted successfully: ID=" + semId + " (Course ID: " + courseId + ")\"}";
        } catch (IllegalArgumentException e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to delete semester: " + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("allsemesters")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Semester> getAllSemesters() {
        try {
            return abl.getAllSemesters();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @GET
    @Path("semestersbycourse/{courseid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Semester> getSemestersByCourse(@PathParam("courseid") Integer courseId) {
        try {
            return abl.getSemestersByCourse(courseId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    //Subject Rest
    @POST
    @Path("addsubject/{subname}/{courseid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addSubject(@PathParam("subname") String subName,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.addSubject(subName, courseId);
            return "{\"status\":\"success\",\"message\":\"Subject added successfully: " + subName + " (Course ID: " + courseId + ")\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @PUT
    @Path("updatesubject/{subid}/{subname}/{courseid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateSubject(@PathParam("subid") Integer subId,
            @PathParam("subname") String subName,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.updateSubject(subId, subName, courseId);
            return "{\"status\":\"success\",\"message\":\"Subject updated successfully: ID=" + subId + ", Name=" + subName + ", Course ID=" + courseId + "\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @DELETE
    @Path("deletesubject/{subid}/{courseid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteSubject(@PathParam("subid") Integer subId,
            @PathParam("courseid") Integer courseId) {
        try {
            abl.deleteSubject(subId, courseId);
            return "{\"status\":\"success\",\"message\":\"Subject deleted successfully: ID=" + subId + " (Course ID=" + courseId + ")\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("allsubjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getAllSubjects() {
        try {
            Collection<Subject> list = abl.getAllSubjects();
            return list;
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Error fetching subjects: " + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("subjectsbycourse/{courseid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getSubjectsByCourse(@PathParam("courseid") Integer courseId) {
        try {
            Collection<Subject> list = abl.getSubjectsByCourse(courseId);
            return list;
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Error fetching subjects by course: " + e.getMessage() + "\"}";
        }
    }
    //Division Rest

    @POST
    @Path("adddivision/{divName}/{semId}/{courseId}")
    public String addDivision(@PathParam("divName") String divName,
            @PathParam("semId") Integer semId,
            @PathParam("courseId") Integer courseId) {
        try {
            abl.addDivision(divName, semId, courseId);
            return "{\"message\": \"Division added successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PUT
    @Path("updatedivision/{divId}/{divName}/{semId}/{courseId}")
    public String updateDivision(@PathParam("divId") Integer divId,
            @PathParam("divName") String divName,
            @PathParam("semId") Integer semId,
            @PathParam("courseId") Integer courseId) {
        try {
            abl.updateDivision(divId, divName, semId, courseId);
            return "{\"message\": \"Division updated successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @DELETE
    @Path("deletedivision/{divId}/{semId}/{courseId}")
    public String deleteDivision(@PathParam("divId") Integer divId,
            @PathParam("semId") Integer semId,
            @PathParam("courseId") Integer courseId) {
        try {
            abl.deleteDivision(divId, semId, courseId);
            return "{\"message\": \"Division deleted successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("getdivisionsbycourse/{courseId}")
    public Collection<Division> getDivisionsByCourse(@PathParam("courseId") Integer courseId) {
        try {
            return abl.getDivisionsByCourse(courseId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @GET
    @Path("getdivisionsbysemester/{semId}")
    public Collection<Division> getDivisionsBySemester(@PathParam("semId") Integer semId) {

        try {
            return abl.getDivisionsBySemester(semId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return java.util.Collections.emptyList(); // Missing return added here
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }

    }

    //Student Rest
    @POST
    @Path("addstudent/{userId}/{rollNo}/{courseId}/{semId}/{divId}")
    @Produces("application/json")
    public String addStudent(
            @PathParam("userId") Integer userId,
            @PathParam("rollNo") Integer rollNo,
            @PathParam("courseId") Integer courseId,
            @PathParam("semId") Integer semId,
            @PathParam("divId") Integer divId) {

        try {
            abl.addStudent(userId, rollNo, courseId, semId, divId);
            return "{\"status\":\"success\",\"message\":\"Student added successfully\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @DELETE
    @Path("deletestudent/{studId}")
    public String deleteStudent(@PathParam("studId") Integer studId) {
        try {
            abl.deleteStudent(studId);
            return "{\"message\": \"Student deleted successfully\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("allstudents")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Student> getAllStudents() {
        try {
            return abl.getAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    @GET
    @Path("studentbyid/{studId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findStudentById(@PathParam("studId") Integer studId) {
        try {
            Student s = abl.findStudentById(studId);
            if (s != null) {
                return s;
            } else {
                return Collections.singletonMap("error", "Invalid Student ID: " + studId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    @GET
    @Path("getstudentsbycourse/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getStudentsByCourse(@PathParam("courseId") Integer courseId) {
        try {
            Collection<Student> students = abl.getStudentsByCourse(courseId);
            if (students == null || students.isEmpty()) {
                return Collections.singletonMap("message", "No students found for Course ID: " + courseId);
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    @GET
    @Path("getstudentsbysem/{semId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getStudentsBySem(@PathParam("semId") Integer semId) {
        try {
            Collection<Student> students = abl.getStudentsBySem(semId);
            if (students == null || students.isEmpty()) {
                return Collections.singletonMap("message", "No students found for Semester ID: " + semId);
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", e.getMessage());
        }
    }
//Attendance Summary Logic

    @GET
    @Path("updatesummary/{studId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object updateSummary(@PathParam("studId") Integer studId) {
        try {
            abl.updateSummary(studId);
            return Collections.singletonMap("message", "Attendance summary updated successfully for Student ID: " + studId);
        } catch (IllegalArgumentException e) {
            return Collections.singletonMap("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", "Error updating summary: " + e.getMessage());
        }
    }

    @GET
    @Path("getsummarybystudent/{studId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getSummaryByStudent(@PathParam("studId") Integer studId) {
        try {
            AttendanceSummary summary = abl.findSummaryByStudent(studId);
            if (summary == null) {
                return Collections.singletonMap("message", "No summary found for Student ID: " + studId);
            }
            return summary;
        } catch (IllegalArgumentException e) {
            return Collections.singletonMap("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", "Error fetching summary: " + e.getMessage());
        }
    }

    @GET
    @Path("getallsummaries")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getAllSummaries() {
        try {
            Collection<AttendanceSummary> list = abl.getAllSummaries();
            if (list == null || list.isEmpty()) {
                return Collections.singletonMap("message", "No attendance summaries found");
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", "Error fetching all summaries: " + e.getMessage());
        }
    }

    @GET
    @Path("getsummarybycourse/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getSummaryByCourse(@PathParam("courseId") Integer courseId) {
        try {
            Collection<AttendanceSummary> list = abl.getSummaryByCourse(courseId);
            if (list == null || list.isEmpty()) {
                return Collections.singletonMap("message", "No summaries found for Course ID: " + courseId);
            }
            return list;
        } catch (IllegalArgumentException e) {
            return Collections.singletonMap("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", "Error fetching summary by course: " + e.getMessage());
        }
    }

    @GET
    @Path("getsummarybysem/{semId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getSummaryBySem(@PathParam("semId") Integer semId) {
        try {
            Collection<AttendanceSummary> list = abl.getSummaryBySemester(semId);
            if (list == null || list.isEmpty()) {
                return Collections.singletonMap("message", "No summaries found for Semester ID: " + semId);
            }
            return list;
        } catch (IllegalArgumentException e) {
            return Collections.singletonMap("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", "Error fetching summary by semester: " + e.getMessage());
        }
    }
    //Registration 

    @POST
    @Path("register/{username}/{password}/{name}/{dob}/{mobile}/{email}/{groupId}")
    @Produces("text/plain")
    public String registerUser(
            @PathParam("username") String username,
            @PathParam("password") String password,
            @PathParam("name") String name,
            @PathParam("dob") String dob, // format: yyyy-MM-dd
            @PathParam("mobile") String mobile,
            @PathParam("email") String email,
            @PathParam("groupId") Integer groupId) {

        try {
            Date date = Date.valueOf(dob);

            abl.registerUser(username, password, name, date, mobile, email, groupId);

            System.out.println("REST API: User registered successfully!");

            return "User registered successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("REST API Error: " + e.getMessage());

            return "Error while registering user: " + e.getMessage();
        }
    }

    @RolesAllowed({"Admin"})
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {

        //TODO return proper representation object
        // throw new UnsupportedOperationException();
        return abl.saySecureHello() + " from Rest Client";
    }
}
