/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import Client.AdminClient;
import EJB.AdminBeanLocal;
import Entity.Course;
import Entity.Users;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.glassfish.soteria.identitystores.hash.PasswordHashCompare;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

/**
 *
 * @author PRAKRUTI
 */
@Named(value = "adminCDIBean")
@RequestScoped
public class AdminCDIBean {

    @EJB
    AdminBeanLocal abl;

    AdminClient ac = new AdminClient();
    Pbkdf2PasswordHashImpl pb;
    PasswordHashCompare phc;
    Response rs;

    //Register 
    Collection<Users> user;
    GenericType<Collection<Users>> guser;
    String username;
    String password;
    String name;
    Date date;
    String mobile;
    String email;

    public AdminCDIBean() {
        pb = new Pbkdf2PasswordHashImpl();
        phc = new PasswordHashCompare();

        user = new ArrayList();
        guser = new GenericType<Collection<Users>>() {
        };
    }

    private static boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public String insertRegiStration() {

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format");
            return null;
        }
        try {
            abl.registerUser(username, password, name, date, mobile, email, 2);

            System.out.println("User registered successfully via EJB!");
            return "Login.jsf";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<Users> getUser() {
        return user;
    }

    public void setUser(Collection<Users> user) {
        this.user = user;
    }

    public GenericType<Collection<Users>> getGuser() {
        return guser;
    }

    public void setGuser(GenericType<Collection<Users>> guser) {
        this.guser = guser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Course fields
    private String message;
    private String courseName;

    // Getter & Setter
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMessage() {
        return message;
    }

    // Method called from form
    public void addCourse() {
        try {
            abl.addCourse(courseName);
            message = "Course added suSccessfully!";
            courseName = ""; // Clear form
        } catch (Exception e) {
            message = "Error: " + e.getMessage();
        }
    }
}
