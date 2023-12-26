package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuperGroupData {

    @SerializedName("Supergroup_Id")
    @Expose
    private String Supergroup_Id;

    @SerializedName("Supergroup_Name")
    @Expose
    private String Supergroup_Name;

    @SerializedName("Supergroup_Image")
    @Expose
    private String Supergroup_Image;

    @SerializedName("Supergroup_Heading")
    @Expose
    private String Supergroup_Heading;

    @SerializedName("groups_name")
    @Expose
    private String groups_name;

    @SerializedName("firebase_topic_name")
    @Expose
    private String firebase_topic_name;

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

    public String getSupergroup_Image() {
        return Supergroup_Image;
    }

    public void setSupergroup_Image(String supergroup_Image) {
        Supergroup_Image = supergroup_Image;
    }

    public String getSupergroup_Heading() {
        return Supergroup_Heading;
    }

    public void setSupergroup_Heading(String supergroup_Heading) {
        Supergroup_Heading = supergroup_Heading;
    }

    public String getGroups_name() {
        return groups_name;
    }

    public void setGroups_name(String groups_name) {
        this.groups_name = groups_name;
    }

    public String getFirebase_topic_name() {
        return firebase_topic_name;
    }

    public void setFirebase_topic_name(String firebase_topic_name) {
        this.firebase_topic_name = firebase_topic_name;
    }
}
