package com.jitendra.videoplex10.Model.YoutubeSearchModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Thumbnails implements Serializable {

    @SerializedName("default")
    public ThumnailsType standard;

    public ThumnailsType medium, high;
}
