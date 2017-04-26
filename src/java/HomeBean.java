
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named(value = "home")
@RequestScoped
@SessionScoped
@ManagedBean
//@ViewScoped

public class HomeBean {

    private String username;
    private String firstname;
    private String mi;
    private String lastname;
    private String chapterSelect;
    private ArrayList<Score> chapterScores;

    public String getUsername() throws SQLException, ClassNotFoundException, IOException {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        username = (String) session.getAttribute("username");
        if (username == null || username.trim().equals("")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
        }
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from USERS WHERE USERNAME = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                setFirstname(rs.getString("FIRSTNAME"));
                setMi(rs.getString("MI"));
                setLastname(rs.getString("LASTNAME"));
            }
        } catch (Exception ex) {
        }finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ArrayList<String> getChapterArray() {
        ArrayList<String> chapters = new ArrayList<>();
        for (int i = 1; i <= 44; i++) {
            chapters.add("Chapter " + i);
        }
        return chapters;
    }

    public String selectChapter() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("username", username);
        String temp = chapterSelect.substring(chapterSelect.indexOf(" "));
        return "Quiz?faces-redirect=true&id=" + temp.trim();
    }

    public String quiz() throws ClassNotFoundException, SQLException, IOException {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("username", getUsername());
        String temp = chapterSelect.substring(chapterSelect.indexOf(" "));
        return "Quiz?faces-redirect=true&id=" + temp.trim();
    }

    public String getChapterSelect() {
        return chapterSelect;
    }

    public void setChapterSelect(String chapterSelect) {

        this.chapterSelect = chapterSelect.substring(chapterSelect.indexOf(" "));
    }

    public String logout() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("username", "");
        return "Login?faces-redirect=true";
    }

    public String scores() throws SQLException, ClassNotFoundException, IOException {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("username", getUsername());
        return "Scores?faces-redirect=true";
    }

    public ArrayList<Score> getChapterScores() throws SQLException, ClassNotFoundException {
        setChapterScores();
        return chapterScores;
    }

    public void setChapterScores() throws SQLException, ClassNotFoundException {
        ArrayList<Score> chapterScores = new ArrayList();
        for (int i = 1; i < 45; i++) {
            chapterScores.add(new Score(i, username));
        }
        this.chapterScores = chapterScores;
    }

}
