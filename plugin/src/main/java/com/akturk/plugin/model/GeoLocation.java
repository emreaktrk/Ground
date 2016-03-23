package com.akturk.plugin.model;

import android.location.Location;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public final class GeoLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("latitude")
    private long mLatitude;

    @SerializedName("longitude")
    private long mLongitude;

    @SerializedName("unlock")
    private boolean mUnlock;

    @SerializedName("range")
    private int mRange;

    public void setId(int id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setLatitude(long latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(long longitude) {
        this.mLongitude = longitude;
    }

    public void setUnlock(boolean unlock) {
        this.mUnlock = unlock;
    }

    public void setRange(int range) {
        this.mRange = range;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public boolean isUnlock() {
        return mUnlock;
    }

    public int getRange() {
        return mRange;
    }

    public Location toLocation() {
        Location location = new Location(mName);
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);

        return location;
    }
}
