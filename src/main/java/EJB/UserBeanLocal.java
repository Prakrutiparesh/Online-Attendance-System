/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Attendance;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author PRAKRUTI
 */
@Local
public interface UserBeanLocal {

    //Attendance Method For Faculty
    public void markAttendance(Integer studId, Integer userId, Integer courseId, Integer subId, Integer semId, Integer divId, Date date, String status);

    public void updateAttendance(Integer attendanceId, String status);

    public void deleteAttendance(Integer attendanceId);

    public Collection<Attendance> getAttendanceByStudent(Integer studId);

    public Collection<Attendance> getAttendanceByDate(Date date);

}
