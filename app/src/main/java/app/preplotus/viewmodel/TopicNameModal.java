package app.preplotus.viewmodel;

public class TopicNameModal {

    private  int image;
    private  int image2;
    private String subTopic;
    private String totalPages;

    public TopicNameModal() {
    }

    public TopicNameModal(int image, int image2, String subTopic, String totalPages) {
        this.image = image;
        this.image2 = image2;
        this.subTopic = subTopic;
        this.totalPages = totalPages;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage2() {
        return image2;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }
}
