package app.preplotus.modelclass;

public class CategoryNameDataModel {
    int img;
    String Subject_name;

    public CategoryNameDataModel(int img, String subject_name) {
        this.img = img;
        Subject_name = subject_name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getSubject_name() {
        return Subject_name;
    }

    public void setSubject_name(String subject_name) {
        Subject_name = subject_name;
    }
}
