package app.preplotus.viewmodel;

public class SubjectNameModal {

    private  int image;
    private String totalsubject;
    private  String topic ;

    public SubjectNameModal() {
    }

    public SubjectNameModal(int image, String totalsubject, String topic) {
        this.image = image;
        this.totalsubject = totalsubject;
        this.topic = topic;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTotalsubject() {
        return totalsubject;
    }

    public void setTotalsubject(String totalsubject) {
        this.totalsubject = totalsubject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
