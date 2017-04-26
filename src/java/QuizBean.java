
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named(value = "quiz")
@RequestScoped
@SessionScoped
@ManagedBean
@ViewScoped

public class QuizBean {

    private int chapter = 0;
    private String id;
    private ArrayList<Question> questionsArray;
    private boolean isAnswered = false;
    private String username = "";
    private String chapterSelect;
    private String submitAll = "";

    public void chapterNoToArray() throws ClassNotFoundException, SQLException {
        questionsArray = new ArrayList();
        questionsArray.add(new Question());
        questionsArray.get(0).setChapterNo(-1);
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM INTRO10EQUIZ WHERE CHAPTERNO = ?");
            ps.setString(1, Integer.toString(chapter));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setChapterNo(rs.getInt("CHAPTERNO"));
                question.setQuestionNo(rs.getInt("QUESTIONO"));
                question.setQuestionText(rs.getString("QUESTION"));
                question.setChoiceA(rs.getString("CHOICEA"));
                question.setChoiceB(rs.getString("CHOICEB"));
                question.setChoiceC(rs.getString("CHOICEC"));
                question.setChoiceD(rs.getString("CHOICED"));
                question.setChoiceE(rs.getString("CHOICEE"));
                question.setAnswerKey(rs.getString("ANSWERKEY"));
                question.setHint(rs.getString("HINT"));
                questionsArray.add(question);
            }
            

            for (int i = 0; i <= questionsArray.size(); i++) {
                ps = con.prepareStatement("SELECT * from INTRO10E WHERE (CHAPTERNO = ?) and (QUESTIONNO = ?) and (USERNAME = ?)");
                ps.setString(1, Integer.toString(chapter));
                ps.setString(2, Integer.toString(i));
                ps.setString(3, getUsername());
                rs = ps.executeQuery();
                while (rs.next()) {
                    questionsArray.get(i).setIsCorrect(rs.getBoolean("iscorrect"));
                    questionsArray.get(i).setSelectedBooleans(rs.getBoolean("ANSWERA"), rs.getBoolean("ANSWERB"), rs.getBoolean("ANSWERC"), rs.getBoolean("ANSWERD"), rs.getBoolean("ANSWERE"));
                    questionsArray.get(i).setRadioOrCheck();
                }
            }
        } catch (Exception ex) {
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public String getId() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        id = paramMap.get("id");
        if (id == null) {
            return Integer.toString(chapter);
        }
        return id;
    }

    public void setId(String id) {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        id = params.get("id");
        chapter = Integer.parseInt(id);
        this.id = id;
    }

    public ArrayList<Question> getQuestionsArray(String i) throws ClassNotFoundException, SQLException {
        chapter = Integer.parseInt(i);
        setQuestionsArray();
        return questionsArray;
    }

    public void setQuestionsArray() throws ClassNotFoundException, SQLException {
        chapterNoToArray();
    }

    public String click(int index) throws ClassNotFoundException, SQLException, IOException {
        if (containsDB(getUsername(), chapter, index )) {
            delete(getUsername(), chapter, index );
            isAnswered = true;
        }
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String host = httpServletRequest.getRemoteAddr();
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("insert into INTRO10E values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, chapter);
            ps.setInt(2, index );
            ps.setBoolean(3, questionsArray.get(index).getIsCorrect());
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            ps.setString(5, host);
            ps.setBoolean(6, questionsArray.get(index).selected.contains("A"));
            ps.setBoolean(7, questionsArray.get(index).selected.contains("B"));
            ps.setBoolean(8, questionsArray.get(index).selected.contains("C"));
            ps.setBoolean(9, questionsArray.get(index).selected.contains("D"));
            ps.setBoolean(10, questionsArray.get(index).selected.contains("E"));
            ps.setString(11, getUsername());

            int i = ps.executeUpdate();
        } catch (Exception ex) {
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }

        return "Quiz?faces-redirect=true&username=" + username + "&id=" + id;
    }

    public boolean isIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String logout() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("username", "");
        return "Login?faces-redirect=true";
    }

    public boolean containsDB(String user, int chapterNo, int questionNo) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from INTRO10E WHERE (CHAPTERNO = ?) and (QUESTIONNO = ?) and (USERNAME = ?)");
            ps.setString(1, Integer.toString(chapterNo));
            ps.setString(2, Integer.toString(questionNo));
            ps.setString(3, user);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return false;
    }

    public void delete(String user, int chapterNo, int questionN0) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        PreparedStatement ps = con.prepareStatement("DELETE from INTRO10E WHERE (CHAPTERNO = ?) and (QUESTIONNO = ?) and (USERNAME = ?)");
        ps.setString(1, Integer.toString(chapterNo));
        ps.setString(2, Integer.toString(questionN0));
        ps.setString(3, user);
        ps.executeUpdate();
        con.close();
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public String getUsername() throws IOException {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        username = (String) session.getAttribute("username");
        if (username == null || username.trim().equals("")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getChapterArray() {
        ArrayList<String> chapters = new ArrayList<>();
        for (int i = 1; i <= 44; i++) {
            chapters.add("Chapter " + i);
        }
        return chapters;
    }

    public void selectChapter() {
        chapter = Integer.parseInt(chapterSelect.trim());
    }

    public String getChapterSelect() {
        return chapterSelect;
    }

    public void setChapterSelect(String chapterSelect) {
        this.chapterSelect = chapterSelect.substring(chapterSelect.indexOf(" "));
    }

    public String getSubmitAll() {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        username = (String) session.getAttribute("submitAll");
        return submitAll;
    }

    public void setSubmitAll(String submitAll) {
        ExternalContext etx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) etx.getRequest();
        HttpSession session = httpReq.getSession();
        session.setAttribute("submitAll", this.submitAll);
        this.submitAll = submitAll;
    }

    public void clickAll() {
        for (int i = 0; i < questionsArray.size(); i++) {
            System.out.println(questionsArray.get(i).getSelected());
        }
        System.out.println("All");
    }

}
