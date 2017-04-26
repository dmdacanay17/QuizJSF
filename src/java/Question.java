
import java.util.ArrayList;
import java.util.List;

public class Question {

    int chapterNo = 0;
    int questionNo = 0;
    String questionText = "";
    String choiceA = "";
    String choiceB = "";
    String choiceC = "";
    String choiceD = "";
    String choiceE = "";
    String answerKey = "";
    String hint = "";
    String selected = "";
    Boolean isCorrect;
    String selectedRadio;
    List<String> selectedCheck = new ArrayList<>();

    public int getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionText() {
        return replace(questionText);
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getChoiceE() {
        return choiceE;
    }

    public void setChoiceE(String choiceE) {
        this.choiceE = choiceE;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    private String replace(String line) {
        String lines[] = line.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = checkKeywords(lines[i]);
            lines[i] = checkString(lines[i]);
            // lines[i] = checkConstants(lines[i]);

        }
        line = "";
        for (int i = 0; i < lines.length; i++) {
            line = line + lines[i] + "<br/>";

        }
        //line = line.replace("\n", "<br/>");
        return line;
    }

    public int getAnswerKeyLength() {
        return this.answerKey.trim().length();
    }

    public int getNumOptions() {
        int i = 0;
        if (this.choiceA.trim().length() > 0) {
            i++;
        }
        if (this.choiceB.trim().length() > 0) {
            i++;
        }
        if (this.choiceC.trim().length() > 0) {
            i++;
        }
        if (this.choiceD.trim().length() > 0) {
            i++;
        }
        if (this.choiceE.trim().length() > 0) {
            i++;
        }

        return i;
    }

    public String getSelectedRadio() {
        return selectedRadio;
    }

    public void setSelectedRadio(String selectedRadio) {
        setSelected(selectedRadio);
        this.selectedRadio = selectedRadio;
    }

    public List<String> getSelectedCheck() {
        return selectedCheck;
    }

    public void setSelectedCheck(List<String> selectedCheck) {
        
        String selected = "";
        for (int i = 0; i < selectedCheck.size(); i++) {
            selected = selected + selectedCheck.get(i);
        }
        setSelected(selected);
        this.selectedCheck = selectedCheck;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return this.selected;
    }

    public Boolean getIsCorrect() {
        if (this.selected.toUpperCase().equals(this.answerKey.toUpperCase())) {
            this.isCorrect = true;
        } else {
            this.isCorrect = false;
        }
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setSelectedBooleans(boolean A, boolean B, boolean C, boolean D, boolean E) {
        String selected = "";
        if (A) {
            selected = selected + "A";
        }
        if (B) {
            selected = selected + "B";
        }
        if (C) {
            selected = selected + "C";
        }
        if (D) {
            selected = selected + "D";
        }
        if (E) {
            selected = selected + "E";
        }

        setSelected(selected);
    }

    public boolean isCorrect() {
        if (this.selected.trim().equals(this.answerKey.trim())) {
            return true;
        }
        return false;
    }

    public void setRadioOrCheck() {
        if (this.answerKey.trim().length() == 1) {
            this.selectedRadio = this.selected;
        } else {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < selected.length(); i++) {
                temp.add(selected.charAt(i) + "");
            }
            setSelectedCheck(temp);
        }
    }

    public boolean getIsAttempted() {
        return !(selected == null || selected.trim().equals(""));
    }

    private String checkKeywords(String line) {
        line = line.replace("public ", "<span style='color:blue;'>public </span>");
        if (line.contains("{")) {
            line = line.replace("class ", "<span style='color:blue;'>class </span>");
        }
        line = line.replace("import ", "<span style='color:blue;'>import </span>");
        line = line.replace("int ", "<span style='color:blue;'>int </span>");
        line = line.replace("double ", "<span style='color:blue;'>double </span>");
        line = line.replace("void ", "<span style='color:blue;'>void </span>");
        line = line.replace("public ", "<span style='color:blue;'>public </span>");
        line = line.replace("static ", "<span style='color:blue;'>static </span>");
        line = line.replace(".out.", ".<span style='color:green;'>out</span>.");
        line = line.replace("if (", "<span style='color:blue;'>if </span>(");
        line = line.replace("if(", "<span style='color:blue;'>if </span>(");
        line = line.replace("else ( ", "<span style='color:blue;'>else </span>(");
        line = line.replace("else( ", "<span style='color:blue;'>else </span>(");
        line = line.replace("new ", "<span style='color:blue;'>new </span>");
        line = line.replace(".in", ".<span style='color:green;'>in</span>");
        line = line.replace("import ", "<span style='color:blue;'>finally </span>");

        line = line.replace("public ", "<span style='color:blue;'>public </span>");

        return line;
    }

    private String checkString(String line) {
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