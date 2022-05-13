package com.jitendra.videoplex10.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceMediaFiles implements Parcelable {
    private String vId, vTitle, vDisplayName, vSize, vDuration, vPath, vDateAdded;

    public DeviceMediaFiles(String vId, String vTitle, String vDisplayName, String vSize, String vDuration, String vPath, String vDateAdded) {
        this.vId = vId;
        this.vTitle = vTitle;
        this.vDisplayName = vDisplayName;
        this.vSize = vSize;
        this.vDuration = vDuration;
        this.vPath = vPath;
        this.vDateAdded = vDateAdded;
    }

    protected DeviceMediaFiles(Parcel in) {
        vId = in.readString();
        vTitle = in.readString();
        vDisplayName = in.readString();
        vSize = in.readString();
        vDuration = in.readString();
        vPath = in.readString();
        vDateAdded = in.readString();
    }

    public static final Creator<DeviceMediaFiles> CREATOR = new Creator<DeviceMediaFiles>() {
        @Override
        public DeviceMediaFiles createFromParcel(Parcel in) {
            return new DeviceMediaFiles(in);
        }

        @Override
        public DeviceMediaFiles[] newArray(int size) {
            return new DeviceMediaFiles[size];
        }
    };

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

    public String getvDisplayName() {
        return vDisplayName;
    }

    public void setvDisplayName(String vDisplayName) {
        this.vDisplayName = vDisplayName;
    }

    public String getvSize() {
        return vSize;
    }

    public void setvSize(String vSize) {
        this.vSize = vSize;
    }

    public String getvDuration() {
        return vDuration;
    }

    public void setvDuration(String vDuration) {
        this.vDuration = vDuration;
    }

    public String getvPath() {
        return vPath;
    }

    public void setvPath(String vPath) {
        this.vPath = vPath;
    }

    public String getvDateAdded() {
        return vDateAdded;
    }

    public void setvDateAdded(String vDateAdded) {
        this.vDateAdded = vDateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vId);
        dest.writeString(vTitle);
        dest.writeString(vDisplayName);
        dest.writeString(vSize);
        dest.writeString(vDuration);
        dest.writeString(vPath);
        dest.writeString(vDateAdded);
    }
}
