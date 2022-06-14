package com.jitendra.videoplex10.Model.YoutubeModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class YtMediaFiles implements Serializable{

    @SerializedName("id")
    public String ytId;

    @SerializedName("snippet")
    public Snippet snippet;

    @SerializedName("contentDetails")
    public ContentDetails contentDetails;

}
