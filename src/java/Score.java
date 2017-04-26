
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Score {

    int chapter;
    int numQuestions = 0;
    int numAttempted = 0;
    int numCorrect = 0;

    Score(int chapter, String username) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM INTRO10EQUIZ WHERE CHAPTERNO = ?");
            ps.setString(1, Integer.toString(chapter));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numQuestions++;
            }

            for (int i = 0; i <= numQuestions; i++) {
                ps = con.prepareStatement("SELECT * from INTRO10E WHERE (CHAPTERNO = ?) and (QUESTIONNO = ?) and (USERNAME = ?)");
                ps.setString(1, Integer.toString(chapter));
                ps.setString(2, Integer.toString(i + 1));
                ps.setString(3, username);
                rs = ps.executeQuery();
                while (rs.next()) {

                    if (rs.getBoolean("ISCORRECT")) {
                        numCorrect++;
                    }

                    if (rs.getBoolean("ANSWERA") || rs.getBoolean("ANSWERB") || rs.getBoolean("ANSWERC") || rs.getBoolean("ANSWERD") || rs.getBoolean("ANSWERE")) {
                        numAttempted++;
                    }
                }
            }
        } catch (Exception ex) {
        }

        this.chapter = chapter;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getNumAttempted() {
        return numAttempted;
    }

    public void setNumAttempted(int numAttempted) {
        this.numAttempted = numAttempted;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }

}
