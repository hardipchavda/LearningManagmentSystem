package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubTopicData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("file_path")
    @Expose
    private String file_path;

    @SerializedName("file_url")
    @Expose
    private String file_url;

    @SerializedName("content_pages_count")
    @Expose
    private String count;

    @SerializedName("is_paid")
    @Expose
    private String is_paid;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }
}
