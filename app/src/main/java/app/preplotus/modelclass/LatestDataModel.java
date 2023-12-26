package app.preplotus.modelclass;

public class LatestDataModel {
    String abc_test_name;
    String time;
    String marks;
    String quetion;

    public LatestDataModel(String abc_test_name, String time, String marks, String quetion) {
        this.abc_test_name = abc_test_name;
        this.time = time;
        this.marks = marks;
        this.quetion = quetion;
    }

    public String getAbc_test_name() {
        return abc_test_name;
    }

    public void setAbc_test_name(String abc_test_name) {
        this.abc_test_name = abc_test_name;
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

    public String getQuetion() {
        return quetion;
    }

    public void setQuetion(String quetion) {
        this.quetion = quetion;
    }
}

