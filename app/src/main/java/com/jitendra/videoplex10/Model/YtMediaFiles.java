package com.jitendra.videoplex10.Model;

import java.io.Serializable;

public class YtMediaFiles {
    private String ytId;

    private String ytVidTitle;

    private String ytChannel;

    private String ytThumbnail;

    private String ytDuration;

    private String ytChannelIcon;

    public YtMediaFiles(String ytId, String ytVidTitle, String ytChannel, String ytThumbnail, String ytDuration, String ytChannelIcon) {
        this.ytId = ytId;
        this.ytVidTitle = ytVidTitle;
        this.ytChannel = ytChannel;
        this.ytThumbnail = ytThumbnail;
        this.ytDuration = ytDuration;
        this.ytChannelIcon = ytChannelIcon;
    }

    public String getYtId() {
        return ytId;
    }

    public void setYtId(String ytId) {
        this.ytId = ytId;
    }

    public String getYtVidTitle() {
        return ytVidTitle;
    }

    public void setYtVidTitle(String ytVidTitle) {
        this.ytVidTitle = ytVidTitle;
    }

    public String getYtChannel() {
        return ytChannel;
    }

    public void setYtChannel(String ytChannel) {
        this.ytChannel = ytChannel;
    }

    public String getYtThumbnail() {
        return ytThumbnail;
    }

    public void setYtThumbnail(String ytThumbnail) {
        this.ytThumbnail = ytThumbnail;
    }

    public String getYtDuration() {
        return ytDuration;
    }

    public void setYtDuration(String ytDuration) {
        this.ytDuration = ytDuration;
    }

    public String getYtChannelIcon() {
        return ytChannelIcon;
    }

    public void setYtChannelIcon(String ytChannelIcon) {
        this.ytChannelIcon = ytChannelIcon;
    }

}
