package com.jitendra.videoplex10.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int vId;

    @ColumnInfo(name = "thumbnail")
    public String vThumbnail;

    @ColumnInfo(name = "Title")
    public String vTitle;

    @ColumnInfo(name = "Channel Name")
    public String vChannelName;
}
