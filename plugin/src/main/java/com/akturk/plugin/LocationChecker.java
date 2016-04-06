package com.akturk.plugin;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.akturk.plugin.helper.BeaconManager;
import com.akturk.plugin.helper.GPSManager;
import com.akturk.plugin.helper.SharedPreferenceEngine;
import com.akturk.plugin.model.Beacon;
import com.akturk.plugin.model.Target;
import com.akturk.plugin.model.TargetList;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;


import java.util.ArrayList;

@SuppressWarnings("unused")
public class LocationChecker implements GPSManager.OnLocationProviderListener, BeaconManager.OnBeaconProviderListener {

    private Activity mActivity;
    private GPSManager mGPSManager;
    private BeaconManager mBeaconManager;

    private String mRawData;
    private TargetList mData;
    private Gson mGson;

    private boolean mInBackground;

    public LocationChecker(final Activity activity) {
        mActivity = activity;

        mGPSManager = new GPSManager(mActivity);
        mGPSManager.setOnLocationProviderListener(this);

        mBeaconManager = new BeaconManager(mActivity);
        mBeaconManager.setOnBeaconProviderListener(this);

        mGson = new Gson();
    }

    public void start(final String rawData) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRawData = rawData;
                mGPSManager.startSeeking();
                mBeaconManager.startSeeking();
            }
        });
    }

    public void stop() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGPSManager.stopSeeking();
                mBeaconManager.stopSeeking();
            }
        });
    }

    @Override
    public void onLocationStartedSeeking() {
        Toast.makeText(mActivity, "Tracking started", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Tracking started");

        mData = mGson.fromJson(mRawData, TargetList.class);

//        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationStartedSeeking", "");
    }

    @Override
    public void onLocationStoppedSeeking() {
        Toast.makeText(mActivity, "Tracking stopped", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Tracking stopped");

//        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationStoppedSeeking", "");
    }

    @Override
    public void onLocationFound(Location location) {
        Toast.makeText(mActivity, "Location found", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Found location > Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());

        for (Target target : mData.getList()) {
            if (target.isUnlock()) {
                Log.d("LOCATION", target.getName() + " is already unlocked");
                continue;
            }

            float distance = target.toLocation().distanceTo(location);
            Log.d("LOCATION", "Distance between to " + target.getName() + " : " + distance);

            if (distance <= target.getRange()) {
                Log.d("LOCATION", target.getName() + " is in range");

                if (target.hasBeacon()) {
                    if (!mBeaconManager.isScanning()) {
                        mBeaconManager.startSeeking();
                    }

                    mBeaconManager.addTarget(target);
                } else {
                    target.setUnlock(true);

//                    UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationFound", target.getId() + "");
                }

            } else {
                Log.d("LOCATION", target.getName() + " is not in range");
            }
        }

    }

    @Override
    public void onGPSProviderDisabled() {
        Toast.makeText(mActivity, "GPS disabled", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "GPS is disabled");

//        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnGPSProviderDisabled", "");
    }

    @Override
    public void onBeaconStartedSeeking() {
        Toast.makeText(mActivity, "Beacon started", Toast.LENGTH_SHORT).show();
        Log.d("BEACON", "Beacon started");
    }

    @Override
    public void onBeaconStoppedSeeking() {
        Toast.makeText(mActivity, "Beacon stopped", Toast.LENGTH_SHORT).show();
        Log.d("BEACON", "Beacon stopped");
    }

    @Override
    public void onBluetoothDisabled() {
        Toast.makeText(mActivity, "Bluetooth disabled", Toast.LENGTH_SHORT).show();
        Log.d("BEACON", "Bluetooth is disabled");
    }

    @Override
    public void onDeviceFound(BluetoothDevice device, Target target) {
        Toast.makeText(mActivity, "Device found", Toast.LENGTH_SHORT).show();
        Log.d("BEACON", "Device found");

//        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationFound", target.getId() + "");
    }

    private void saveData() {
        String rawData = mGson.toJson(mData);
        SharedPreferenceEngine
                .getInstance(mActivity)
                .edit()
                .putString(SharedPreferenceEngine.LOCATIONS, rawData)
                .apply();

        Toast.makeText(mActivity, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public boolean isInBackground() {
        return mInBackground;
    }

    public void setInBackground(boolean background) {
        this.mInBackground = background;
    }

    private String getDummyLocations() {
        Target locationFirst = new Target();
        locationFirst.setId(0);
        locationFirst.setName("Point 1");
        locationFirst.setRange(1000);
        locationFirst.setUnlock(false);
        locationFirst.setLatitude(40);
        locationFirst.setLongitude(40);
        locationFirst.setAltitude(100);

        ArrayList<Beacon> beacons = new ArrayList<>();
        Beacon beacon = new Beacon();
        beacon.setMajor(9);
        beacon.setMinor(1);
        beacon.setUUID("760D2862-F66A-4AC5-A2D1-3E303C32030E");
        beacons.add(beacon);

        Target locationSecond = new Target();
        locationSecond.setId(1);
        locationSecond.setName("Point 2");
        locationSecond.setRange(5000);
        locationSecond.setUnlock(true);
        locationSecond.setLatitude(50);
        locationSecond.setLongitude(50);
        locationSecond.setAltitude(0);
        locationSecond.setBeacons(beacons);


        ArrayList<Target> list = new ArrayList<>();
        list.add(locationFirst);
        list.add(locationSecond);

        TargetList geoLocationList = new TargetList();
        geoLocationList.setList(list);

        return mGson.toJson(geoLocationList);
    }
}
