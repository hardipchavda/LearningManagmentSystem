package app.preplotus.modelclass;

public class MockDataModel {

    String mock_test_no;
    String time;
    String  marks;
    String question;
    String btn;

    public MockDataModel(String mock_test_no, String time, String marks, String question, String btn) {
        this.mock_test_no = mock_test_no;
        this.time = time;
        this.marks = marks;
        this.question = question;
        this.btn = btn;
    }

    public String getMock_test_no() {
        return mock_test_no;
    }

    public void setMock_test_no(String mock_test_no) {
        this.mock_test_no = mock_test_no;
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
