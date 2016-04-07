package com.akturk.plugin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public final class Beacon implements Serializable {

    @SerializedName("minor")
    private int mMinor;

    @SerializedName("major")
    private int mMajor;

    @SerializedName("uuid")
    private String mUUID;

    @SerializedName("range")
    private int mRange;

    @SerializedName("adress")
    private String mAdress;

    public int getMinor() {
        return mMinor;
    }

    public void setMinor(int minor) {
        this.mMinor = minor;
    }

    public int getMajor() {
        return mMajor;
    }

    public void setMajor(int major) {
        this.mMajor = major;
    }

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String uuid) {
        this.mUUID = uuid;
    }

    public int getRange() {
        return mRange;
    }

    public void setRange(int range) {
        this.mRange = range;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String adress) {
        this.mAdress = adress;
    }
}
