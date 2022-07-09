package com.jitendra.videoplex10.VidDatabase.WatchHistoryDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"HistoryId"}, unique = true)})
public class HistoryVids {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Seq")
    public int seq;

    @ColumnInfo(name = "HistoryId")
    public String hvId;

    @ColumnInfo(name = "HistoryTitle")
    public String hvTitle;

    @ColumnInfo(name = "HistoryChannelName")
    public String hvChannelName;

    @ColumnInfo(name = "HistoryThumbnail")
    public String hvThumbnail;

    @ColumnInfo(name = "HistoryDuration")
    public String hvDuration;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getHvId() {
        return hvId;
    }

    public void setHvId(String hvId) {
        this.hvId = hvId;
    }

    public String getHvTitle() {
        return hvTitle;
    }

    public void setHvTitle(String hvTitle) {
        this.hvTitle = hvTitle;
    }

    public String getHvChannelName() {
        return hvChannelName;
    }

    public void setHvChannelName(String hvChannelName) {
        this.hvChannelName = hvChannelName;
    }

    public String getHvThumbnail() {
        return hvThumbnail;
    }

    public void setHvThumbnail(String hvThumbnail) {
        this.hvThumbnail = hvThumbnail;
    }

    public String getHvDuration() {
        return hvDuration;
    }

    public void setHvDuration(String hvDuration) {
        this.hvDuration = hvDuration;
    }
}
