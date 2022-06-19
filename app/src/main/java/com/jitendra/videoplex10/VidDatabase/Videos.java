package com.jitendra.videoplex10.VidDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Videos {


    @NonNull
    @PrimaryKey
    public String vId;

    @ColumnInfo(name = "Title")
    public String vTitle;

    @ColumnInfo(name = "Channel Name")
    public String vChannelName;

    @ColumnInfo(name = "Thumbnail")
    public String vThumbnail;

    @ColumnInfo(name = "Duration")
    public String vDuration;

    public Videos(String vId, String vTitle, String vChannelName, String vThumbnail, String vDuration) {
        this.vId = vId;
        this.vTitle = vTitle;
        this.vChannelName = vChannelName;
        this.vThumbnail = vThumbnail;
        this.vDuration = vDuration;
    }

    public Videos() {

    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getvTitle() {
        return vTitle;
    }

    public void setvTitle(String vTitle) {
        this.vTitle = vTitle;
    }

    public String getvChannelName() {
        return vChannelName;
    }

    public void setvChannelName(String vChannelName) {
        this.vChannelName = vChannelName;
    }

    public String getvThumbnail() {
        return vThumbnail;
    }

    public void setvThumbnail(String vThumbnail) {
        this.vThumbnail = vThumbnail;
    }

    public String getvDuration() {
        return vDuration;
    }

    public void setvDuration(String vDuration) {
        this.vDuration = vDuration;
    }
}
