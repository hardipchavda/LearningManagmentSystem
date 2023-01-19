package app.preplotus.modelclass;

public class ViewDataModel {
    int image;
    String package_name;
    String explore;

    public ViewDataModel(int image, String package_name, String explore) {
        this.image = image;
        this.package_name = package_name;
        this.explore = explore;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getExplore() {
        return explore;
    }

    public void setExplore(String explore) {
        this.explore = explore;
    }
}
