
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named(value = "create")
@RequestScoped
@SessionScoped
@ManagedBean
//@ViewScoped

public class CreateBean {

    private String firstname;
    private String lastname;
    private String mi;
    private String username;
    private String password;
    private String confirmPassword;
    private String flag = "";

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String submit() throws ClassNotFoundException, SQLException {
        boolean go = true;
        if (containsUserDB(username)) {
            flag = flag + "Username Already Exists \n";
            go = false;
        }
        if (firstname.trim().equals("")) {
            flag = flag + "First Name Cannot be blank \n";
            go = false;
        }
        if (lastname.trim().equals("")) {
            flag = flag + "Last Name Cannot be blank \n";
            go = false;
        }
        if (mi.trim().equals("")) {
            flag = flag + "MI Cannot be blank \n";
            go = false;
        }
        if (username.trim().equals("")) {
            flag = flag + "Username Cannot be blank \n";
            go = false;
        }
        if (password.trim().equals("")) {
            flag = flag + "Password Cannot be blank \n";
            go = false;
        }
        if (confirmPassword.trim().equals("")) {
            flag = flag + "Confirm Password Cannot be blank \n";
            go = false;
        }
        if (!password.trim().equals(confirmPassword.trim())) {
            flag = flag + "Passwords don't match \n";
            go = false;
        }
        if (go) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
            //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
            PreparedStatement ps = con.prepareStatement("insert into users values(?,?,?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, firstname);
            ps.setString(3, mi);
            ps.setString(4, lastname);
            ps.setString(5, password);
            ps.setTimestamp(6, new Timestamp(new Date().getTime()));
            int i = ps.executeUpdate();
            flag = "Account Created";
        }

        return "";
    }

    public String back() {
        return "Login?faces-redirect=true";
    }

    public boolean containsUserDB(String user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
       // Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from USERS WHERE USERNAME = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
        }
        return false;
    }

}
