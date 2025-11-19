/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:AdminRest [Admin]<br>
 * USAGE:
 * <pre>
 *        AdminClient client = new AdminClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author PRAKRUTI
 */
public class AdminClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Online-Attendance-System/resources";

    public AdminClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("Admin");
    }

    public <T> T getAllSubjects(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("allsubjects");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T updateSummary(Class<T> responseType, String studId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("updatesummary/{0}", new Object[]{studId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String addDivision(String divName, String semId, String courseId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("adddivision/{0}/{1}/{2}", new Object[]{divName, semId, courseId})).request().post(null, String.class);
    }

    public <T> T getAllCourse(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("allcourse");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String deleteCourse(String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletecourse/{0}", new Object[]{courseid})).request().delete(String.class);
    }

    public <T> T getStudentsBySem(Class<T> responseType, String semId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getstudentsbysem/{0}", new Object[]{semId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getCourse(Class<T> responseType, String courseid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findcourse/{0}", new Object[]{courseid}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String updateDivision(String divId, String divName, String semId, String courseId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updatedivision/{0}/{1}/{2}/{3}", new Object[]{divId, divName, semId, courseId})).request().put(null, String.class);
    }

    public <T> T getSubjectsByCourse(Class<T> responseType, String courseid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("subjectsbycourse/{0}", new Object[]{courseid}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getSummaryByStudent(Class<T> responseType, String studId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getsummarybystudent/{0}", new Object[]{studId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String addSemester(String semname, String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addsemester/{0}/{1}", new Object[]{semname, courseid})).request().post(null, String.class);
    }

    public <T> T getDivisionsByCourse(Class<T> responseType, String courseId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getdivisionsbycourse/{0}", new Object[]{courseId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String deleteStudent(String studId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletestudent/{0}", new Object[]{studId})).request().delete(String.class);
    }

    public <T> T getSummaryByCourse(Class<T> responseType, String courseId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getsummarybycourse/{0}", new Object[]{courseId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String updateSubject(String subid, String subname, String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updatesubject/{0}/{1}/{2}", new Object[]{subid, subname, courseid})).request().put(null, String.class);
    }

    public String addStudent(String userId, String rollNo, String courseId, String semId, String divId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addstudent/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, rollNo, courseId, semId, divId})).request().post(null, String.class);
    }

    public String sayHello() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(jakarta.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public String addCourse(String coursename) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addcourse/{0}", new Object[]{coursename})).request().post(null, String.class);
    }

    public String updateCourse(String courseid, String coursename) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updatecourse/{0}/{1}", new Object[]{courseid, coursename})).request().put(null, String.class);
    }

    public String deleteSemester(String semid, String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletesemester/{0}/{1}", new Object[]{semid, courseid})).request().delete(String.class);
    }

    public String deleteDivision(String divId, String semId, String courseId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletedivision/{0}/{1}/{2}", new Object[]{divId, semId, courseId})).request().delete(String.class);
    }

    public <T> T getAllStudents(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("allstudents");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String addSubject(String subname, String courseid, String semid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addsubject/{0}/{1}/{2}", new Object[]{subname, courseid, semid})).request().post(null, String.class);
    }

    public <T> T getSummaryBySem(Class<T> responseType, String semId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getsummarybysem/{0}", new Object[]{semId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String deleteSubject(String subid, String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletesubject/{0}/{1}", new Object[]{subid, courseid})).request().delete(String.class);
    }

    public String registerUser(String username, String password, String name, String dob, String mobile, String email, String groupId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("register/{0}/{1}/{2}/{3}/{4}/{5}/{6}", new Object[]{username, password, name, dob, mobile, email, groupId})).request().post(null, String.class);
    }

    public <T> T getAllSemesters(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("allsemesters");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getSemestersByCourse(Class<T> responseType, String courseid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("semestersbycourse/{0}", new Object[]{courseid}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findCouseByName(Class<T> responseType, String coursename) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findcoursename/{0}", new Object[]{coursename}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String updateSemester(String semid, String semname, String courseid) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updatesemester/{0}/{1}/{2}", new Object[]{semid, semname, courseid})).request().put(null, String.class);
    }

    public <T> T getDivisionsBySemester(Class<T> responseType, String semId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getdivisionsbysemester/{0}", new Object[]{semId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getStudentsByCourse(Class<T> responseType, String courseId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getstudentsbycourse/{0}", new Object[]{courseId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findStudentById(Class<T> responseType, String studId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("studentbyid/{0}", new Object[]{studId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllSummaries(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getallsummaries");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
