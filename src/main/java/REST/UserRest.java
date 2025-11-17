/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package REST;

import EJB.UserBeanLocal;
import Entity.Attendance;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author PRAKRUTI
 */
@Path("User")
public class UserRest {
    //Attendance Rest

    @EJB
    UserBeanLocal ubl;

    @POST
    @Path("markattendance/{studId}/{userId}/{courseId}/{subId}/{semId}/{divId}/{date}/{status}")
    public String markAttendance(
            @PathParam("studId") Integer studId,
            @PathParam("userId") Integer userId,
            @PathParam("courseId") Integer courseId,
            @PathParam("subId") Integer subId,
            @PathParam("semId") Integer semId,
            @PathParam("divId") Integer divId,
            @PathParam("date") String dateStr,
            @PathParam("status") String status) {

        try {

            java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);  // yyyy-MM-dd format required

            ubl.markAttendance(studId, userId, courseId, subId, semId, divId, sqlDate, status);

            return "{\"status\":\"success\",\"message\":\"Attendance marked successfully for Student ID: " + studId + "\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @PUT
    @Path("updateattendance/{attendanceId}/{status}")
    public String updateAttendance(
            @PathParam("attendanceId") Integer attendanceId,
            @PathParam("status") String status) {
        try {
            ubl.updateAttendance(attendanceId, status);
            return "{\"status\":\"success\",\"message\":\"Attendance updated successfully for ID: " + attendanceId + "\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @DELETE
    @Path("deleteattendance/{attendanceId}")
    public String deleteAttendance(@PathParam("attendanceId") Integer attendanceId) {
        try {
            ubl.deleteAttendance(attendanceId);
            return "{\"status\":\"success\",\"message\":\"Attendance deleted successfully for ID: " + attendanceId + "\"}";
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Unexpected error: " + e.getMessage() + "\"}";
        }
    }

    @GET
    @Path("getbyStudent/{studId}")
    public Object getAttendanceByStudent(@PathParam("studId") Integer studId) {
        try {
            Collection<Attendance> list = ubl.getAttendanceByStudent(studId);
            if (list == null || list.isEmpty()) {
                return Collections.singletonMap("message", "No attendance records found for Student ID: " + studId);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", e.getMessage());
        }

    }

    @GET
    @Path("getbyDate/{date}")
    public Object getAttendanceByDate(@PathParam("date") String dateStr) {
        try {
            java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Collection<Attendance> list = ubl.getAttendanceByDate(sqlDate);

            if (list == null || list.isEmpty()) {
                return Collections.singletonMap("message", "No attendance records found for date: " + dateStr);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("error", e.getMessage());
        }
    }

}
