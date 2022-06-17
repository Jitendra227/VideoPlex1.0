package com.jitendra.videoplex10.Model.YoutubeSearchModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Snippet implements Serializable {

    @SerializedName("title")
    public String title;

    @SerializedName("channelTitle")
    public String channelTitle;

    @SerializedName("description")
    public String description;

    @SerializedName("thumbnails")
    public Thumbnails thumbnails;
}
