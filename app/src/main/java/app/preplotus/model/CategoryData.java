package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryData {

    @SerializedName("CatName")
    @Expose
    private String CatName;

    @SerializedName("CatImage")
    @Expose
    private String CatImage;

    @SerializedName("CatDescription")
    @Expose
    private String CatDescription;

    @SerializedName("CatId")
    @Expose
    private String catId;

    @SerializedName("Firebase_topic_name")
    @Expose
    private String Firebase_topic_name;

    @SerializedName("firebase_topic_name")
    @Expose
    private String topic_name;

    @SerializedName("Supergroup_Id")
    @Expose
    private String Supergroup_Id;

    @SerializedName("Supergroup_Name")
    @Expose
    private String Supergroup_Name;

    @SerializedName("Supergroup_firebase_Name")
    @Expose
    private String Supergroup_firebase_Name;

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public String getCatImage() {
        return CatImage;
    }

    public void setCatImage(String catImage) {
        CatImage = catImage;
    }

    public String getCatDescription() {
        return CatDescription;
    }

    public void setCatDescription(String catDescription) {
        CatDescription = catDescription;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getFirebase_topic_name() {
        return Firebase_topic_name;
    }

    public void setFirebase_topic_name(String firebase_topic_name) {
        Firebase_topic_name = firebase_topic_name;
    }

    public String getSupergroup_Id() {
        return Supergroup_Id;
    }

    public void setSupergroup_Id(String supergroup_Id) {
        Supergroup_Id = supergroup_Id;
    }

    public String getSupergroup_Name() {
        return Supergroup_Name;
    }

    public void setSupergroup_Name(String supergroup_Name) {
        Supergroup_Name = supergroup_Name;
    }

    public String getSupergroup_firebase_Name() {
        return Supergroup_firebase_Name;
    }

    public void setSupergroup_firebase_Name(String supergroup_firebase_Name) {
        Supergroup_firebase_Name = supergroup_firebase_Name;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }
}
