package com.akturk.plugin.model;

import android.location.Location;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class Target implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("latitude")
    private long mLatitude;

    @SerializedName("longitude")
    private long mLongitude;

    @SerializedName("altitude")
    private long mAltitude;

    @SerializedName("unlock")
    private boolean mUnlock;

    @SerializedName("range")
    private int mRange;

    @SerializedName("beacons")
    private ArrayList<Beacon> mBeacons;

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

    public long getAltitude() {
        return mAltitude;
    }

    public void setAltitude(long altitude) {
        this.mAltitude = altitude;
    }

    public ArrayList<Beacon> getBeacons() {
        return mBeacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.mBeacons = beacons;
    }

    public boolean hasBeacon() {
        return this.mBeacons != null && this.mBeacons.size() > 0;
    }

    public Location toLocation() {
        Location location = new Location(mName);
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);

        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Target target = (Target) o;

        if (mId != target.mId) return false;
        if (mLatitude != target.mLatitude) return false;
        if (mLongitude != target.mLongitude) return false;
        if (mAltitude != target.mAltitude) return false;
        if (mUnlock != target.mUnlock) return false;
        if (mRange != target.mRange) return false;
        if (mName != null ? !mName.equals(target.mName) : target.mName != null) return false;

        return mBeacons != null ? mBeacons.equals(target.mBeacons) : target.mBeacons == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (int) (mLatitude ^ (mLatitude >>> 32));
        result = 31 * result + (int) (mLongitude ^ (mLongitude >>> 32));
        result = 31 * result + (int) (mAltitude ^ (mAltitude >>> 32));
        result = 31 * result + (mUnlock ? 1 : 0);
        result = 31 * result + mRange;
        result = 31 * result + (mBeacons != null ? mBeacons.hashCode() : 0);

        return result;
    }
}
