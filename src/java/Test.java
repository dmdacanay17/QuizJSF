
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dustin Dacanay
 */
public class Test {
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        String t = "System.out.println(\"Welcome to Java!\");";
        String[] s = t.split("\"");
        for(int i = 0; i < s.length;i++){
            System.out.println(s[i]);
        }
        System.out.println(checkString(t));
        
        
    }
    
    
    public static String checkString(String line) {
        System.out.println((line.split("\"").length));
        if ((line.split("\"").length - 1) % 2 == 0) {
            String[] lineSplit = line.split("\"");
            line = "";
            for (int i = 0; i < lineSplit.length; i++) {
                if (i % 2 == 0) {
                    line =line + lineSplit[i];
                }else{
                    line = line + "<span style='color:orange;'>\"" + lineSplit[i] + "\"</span>";
                }
            }
            return line;
        }
        return line;
    }
    
}
