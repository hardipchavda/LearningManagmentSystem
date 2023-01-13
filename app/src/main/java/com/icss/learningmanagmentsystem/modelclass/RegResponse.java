package com.icss.learningmanagmentsystem.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegResponse {

    @SerializedName("responce")
    @Expose
    public Boolean responce;
    @SerializedName("massage")
    @Expose
    public RegDataModel massage;
}
