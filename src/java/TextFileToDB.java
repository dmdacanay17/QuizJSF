import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextFileToDB {

    public static void main(String args[]) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:mysql://liang.armstrong.edu:3306/dacanay", "dacanay", "tiger");
        //Connection con = DriverManager.getConnection("jdbc:derby://localhost/Dacanay", "dustin", "dustin");
        System.out.println("DB Connected");
        PreparedStatement ps;

        int chapterNo;
        int questionNo;
        String questionText;
        String choiceA;
        String choiceB;
        String choiceC;
        String choiceD;
        String choiceE;
        String answerKey;
        String hint;

        for (chapterNo = 1; chapterNo < 45; chapterNo++) {
            System.out.println(chapterNo);
            try (BufferedReader reader = new BufferedReader(new FileReader("mcquestions/chapter" + chapterNo + ".txt"))) {
                String line;
                questionNo = 1;
                while ((line = reader.readLine()) != null) {
                    questionText = "";
                    choiceA = "";
                    choiceB = "";
                    choiceC = "";
                    choiceD = "";
                    choiceE = "";
                    answerKey = "";
                    hint = "";
                    line = line + "     ";
                    if (line.substring(0, 3).contains(questionNo + ".")) {

                        questionText = line;
                        line = reader.readLine();
                        while (!line.contains("KEY:")) {
                            line = line + "    ";
                            if (line.charAt(0) == 'a' && line.charAt(1) == '.') {
                                choiceA = line;
                            } else if (line.charAt(0) == 'b' && line.charAt(1) == '.') {
                                choiceB = line;
                            } else if (line.charAt(0) == 'c' && line.charAt(1) == '.') {
                                choiceC = line;
                            } else if (line.charAt(0) == 'd' && line.charAt(1) == '.') {
                                choiceD = line;
                            } else if (line.charAt(0) == 'e' && line.charAt(1) == '.') {
                                choiceE = line;
                            } else {
                                questionText = questionText + "\n" + line;
                            }
                            line = reader.readLine();
                        }

                        for (int j = 4; j < line.length(); j++) {

                            if (line.charAt(j) == ' ') {
                                hint = line.substring(j + 1);
                                j = line.length();
                            } else {
                                answerKey = answerKey + line.charAt(j);
                            }
                        }

                        ps = con.prepareStatement("insert into INTRO10EQUIZ values(?,?,?,?,?,?,?,?,?,?)");

                        ps.setInt(1, chapterNo);
                        ps.setInt(2, questionNo);
                        ps.setString(3, questionText);
                        ps.setString(4, choiceA);
                        ps.setString(5, choiceB);
                        ps.setString(6, choiceC);
                        ps.setString(7, choiceD);
                        ps.setString(8, choiceE);
                        ps.setString(9, answerKey);
                        ps.setString(10, hint);
                        int i = ps.executeUpdate();
                        questionNo++;
                    }

                }
            }
        }
    }
}
