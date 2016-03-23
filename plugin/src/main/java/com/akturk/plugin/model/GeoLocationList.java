package com.akturk.plugin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class GeoLocationList implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("scenarios")
    private ArrayList<GeoLocation> mList;

    public ArrayList<GeoLocation> getList() {
        return mList;
    }

    public void setList(ArrayList<GeoLocation> list) {
        this.mList = list;
    }
}
