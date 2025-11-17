/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

/**
 *
 * @author PRAKRUTI
 */
@Named(value = "userCDIBean")
@RequestScoped
public class UserCDIBean {

//    @EJB
//    UserBeanLocal ubl;
//
//    AdminClient ac = new AdminClient();
//    Pbkdf2PasswordHashImpl pb;
//    PasswordHashCompare phc;
//    Response rs;
//    Collection<Users> user;
//    GenericType<Collection<Users>> guser;
//    String username;
//    String password;
//    String name;
//    Date date;
//    String mobile;
//    String email;
//
//    public UserCDIBean() {
//        pb = new Pbkdf2PasswordHashImpl();
//        phc = new PasswordHashCompare();
//
//        user = new ArrayList();
//        guser = new GenericType<Collection<Users>>() {
//        };
//    }
//
//    private static boolean isValidEmail(String email) {
//
//        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        Pattern pattern = Pattern.compile(emailRegex);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//
//    }
//
//    public String insertRegiStration() {
//        if (!isValidEmail(email)) {
//            System.out.println("Invalid email format");
//            return null;
//        }
//        try {
//            // Convert date to yyyy-MM-dd
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String dobString = sdf.format(date);
//
//            // Build REST API URL (same as before)
//            String apiUrl = "http://localhost:8080/Online-Attendance-System/resources/User/register/"
//                    + username + "/" + password + "/" + name + "/"
//                    + dobString + "/" + mobile + "/" + email + "/1";
//
//            System.out.println("Calling REST API: " + apiUrl);
//
//            Client client = ClientBuilder.newClient();
//
//            // Use POST instead of GET
//            Response response = client.target(apiUrl)
//                    .request(MediaType.TEXT_PLAIN)
//                    .post(Entity.text("")); // Empty body because all data is in PathParams
//
//            String result = response.readEntity(String.class);
//            System.out.println("REST API Response: " + result);
//
//            if (result.contains("successfully")) {
//                return "login.jsf";
//            } else {
//                System.out.println("Error from REST API: " + result);
//                return null;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//    public Collection<Users> getUser() {
//        return user;
//    }
//
//    public void setUser(Collection<Users> user) {
//        this.user = user;
//    }
//
//    public GenericType<Collection<Users>> getGuser() {
//        return guser;
//    }
//
//    public void setGuser(GenericType<Collection<Users>> guser) {
//        this.guser = guser;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

}
