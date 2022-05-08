package com.jitendra.videoplex10.Model;

import android.graphics.Bitmap;

public class DeviceVideoItems {
    private String vidName, vidPath, vidThumbNail;

    public DeviceVideoItems(String vidName, String vidPath, String vidThumbNail) {
        this.vidName = vidName;
        this.vidPath = vidPath;
        this.vidThumbNail = vidThumbNail;
    }

    public String getVidName() {
        return vidName;
    }

    public void setVidName(String vidName) {
        this.vidName = vidName;
    }

    public String getVidPath() {
        return vidPath;
    }

    public void setVidPath(String vidPath) {
        this.vidPath = vidPath;
    }

    public String getVidThumbNail() {
        return vidThumbNail;
    }

    public void setVidThumbNail(String vidThumbNail) {
        this.vidThumbNail = vidThumbNail;
    }

}
