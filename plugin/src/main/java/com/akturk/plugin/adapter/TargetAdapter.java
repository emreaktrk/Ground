package com.akturk.plugin.adapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class TargetAdapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public String mId;

    @SerializedName("name")
    public String mName;

    @SerializedName("latitude")
    public String mLatitude;

    @SerializedName("longitude")
    public String mLongitude;

    @SerializedName("altitude")
    public String mAltitude;

    @SerializedName("unlock")
    public String mUnlock;

    @SerializedName("range")
    public String mRange;

    @SerializedName("beacons")
    public ArrayList<BeaconAdapter> mBeacons;

}
