package com.akturk.plugin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class TargetList implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("Data")
    private ArrayList<Target> mList;

    public ArrayList<Target> getList() {
        return mList;
    }

    public void setList(ArrayList<Target> list) {
        this.mList = list;
    }
}
