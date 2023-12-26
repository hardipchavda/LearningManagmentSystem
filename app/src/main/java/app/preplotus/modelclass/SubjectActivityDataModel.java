package app.preplotus.modelclass;

public class SubjectActivityDataModel {

    String Topic_name;
    String  time;
    String marks;
    String question;
    String btn ;

    public SubjectActivityDataModel(String topic_name, String time, String marks, String question, String btn) {
        Topic_name = topic_name;
        this.time = time;
        this.marks = marks;
        this.question = question;
        this.btn = btn;
    }

    public String getTopic_name() {
        return Topic_name;
    }

    public void setTopic_name(String topic_name) {
        Topic_name = topic_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }
}



