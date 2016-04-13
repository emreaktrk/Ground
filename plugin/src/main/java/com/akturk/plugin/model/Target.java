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
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("altitude")
    private double mAltitude;

    @SerializedName("unlock")
    private boolean mUnlock;

    @SerializedName("range")
    private double mRange;

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

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public boolean isUnlock() {
        return mUnlock;
    }

    public double getRange() {
        return mRange;
    }

    public double getRangeAsMeters() {
        return mRange * 1000;
    }

    public double getAltitude() {
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

        return mId == target.mId;

    }

    @Override
    public int hashCode() {
        return mId;
    }
}
