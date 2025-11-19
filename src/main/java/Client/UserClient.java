/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:UserRest [User]<br>
 * USAGE:
 * <pre>
 *        UserClient client = new UserClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author PRAKRUTI
 */
public class UserClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://localhost:8181/Online-Attendance-System/resources";

    public UserClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("User");
    }

    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost")) {
                    return true;
                }
                return false;
            }
        });
    }

    public <T> T getAttendanceByDate(Class<T> responseType, String date) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getbyDate/{0}", new Object[]{date}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String deleteAttendance(String attendanceId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deleteattendance/{0}", new Object[]{attendanceId})).request().delete(String.class);
    }

    public String markAttendance(String studId, String userId, String courseId, String subId, String semId, String divId, String date, String status) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("markattendance/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}", new Object[]{studId, userId, courseId, subId, semId, divId, date, status})).request().post(null, String.class);
    }

    public String updateAttendance(String attendanceId, String status) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updateattendance/{0}/{1}", new Object[]{attendanceId, status})).request().put(null, String.class);
    }

    public <T> T getAttendanceByStudent(Class<T> responseType, String studId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getbyStudent/{0}", new Object[]{studId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }

}
