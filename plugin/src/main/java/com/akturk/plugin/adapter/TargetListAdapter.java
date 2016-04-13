package com.akturk.plugin.adapter;

import com.akturk.plugin.model.Beacon;
import com.akturk.plugin.model.Target;
import com.akturk.plugin.model.TargetList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class TargetListAdapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("Data")
    private ArrayList<TargetAdapter> mList;

    public ArrayList<TargetAdapter> getList() {
        return mList;
    }

    public void setList(ArrayList<TargetAdapter> list) {
        this.mList = list;
    }

    public TargetList adapt() {
        ArrayList<Target> targetItems = new ArrayList<>();
        for (TargetAdapter adaptItem : mList) {
            Target item = new Target();
            item.setId(Integer.parseInt(adaptItem.mId));
            item.setName(adaptItem.mName);
            item.setLatitude(Long.parseLong(adaptItem.mLatitude));
            item.setLongitude(Long.parseLong(adaptItem.mLongitude));
            item.setAltitude(Long.parseLong(adaptItem.mAltitude));
            item.setUnlock(Boolean.parseBoolean(adaptItem.mUnlock));
            item.setRange(Integer.parseInt(adaptItem.mRange));

            if (adaptItem.mBeacons == null) {
                targetItems.add(item);
                continue;
            }

            ArrayList<Beacon> beaconItems = new ArrayList<>();
            for (BeaconAdapter beaconAdapt : adaptItem.mBeacons) {
                Beacon beacon = new Beacon();
                beacon.setMinor(Integer.parseInt(beaconAdapt.mMinor));
                beacon.setMajor(Integer.parseInt(beaconAdapt.mMajor));
                beacon.setUUID(beaconAdapt.mUUID);
                beacon.setRange(Integer.parseInt(beaconAdapt.mRange));
                beacon.setAdress(beaconAdapt.mAdress);
                beaconItems.add(beacon);
            }

            item.setBeacons(beaconItems);
            targetItems.add(item);
        }

        TargetList targetListItem = new TargetList();
        targetListItem.setList(targetItems);
        return targetListItem;
    }
}
