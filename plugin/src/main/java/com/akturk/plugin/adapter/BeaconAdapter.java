package com.akturk.plugin.adapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public final class BeaconAdapter implements Serializable {

    @SerializedName("minor")
    public String mMinor;

    @SerializedName("major")
    public String mMajor;

    @SerializedName("uuid")
    public String mUUID;

    @SerializedName("range")
    public String mRange;

    @SerializedName("adress")
    public String mAdress;

}
