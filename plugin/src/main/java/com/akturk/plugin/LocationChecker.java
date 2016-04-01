package com.akturk.plugin;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.akturk.plugin.helper.LocationProvider;
import com.akturk.plugin.helper.SharedPreferenceEngine;
import com.akturk.plugin.model.GeoLocation;
import com.akturk.plugin.model.GeoLocationList;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class LocationChecker implements LocationProvider.OnLocationProviderListener {

    private Activity mActivity;
    private LocationProvider mProvider;

    private String mRawData;
    private GeoLocationList mData;
    private Gson mGson;

    public LocationChecker(final Activity activity) {
        mActivity = activity;
        mProvider = new LocationProvider(mActivity);
        mProvider.setOnLocationProviderListener(this);

        mGson = new Gson();
    }

    public void start(final String rawData) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRawData = rawData;
                mProvider.startSeeking();
            }
        });
    }

    public void stop() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProvider.stopSeeking();
            }
        });
    }

    @Override
    public void onLocationStartedSeeking() {
        Toast.makeText(mActivity, "Tracking started", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Tracking started");

//        String rawData = SharedPreferenceEngine.getInstance(mActivity).getString(SharedPreferenceEngine.LOCATIONS, getDummyLocations());
        mData = mGson.fromJson(mRawData, GeoLocationList.class);

        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationStartedSeeking", "");
    }

    @Override
    public void onLocationStoppedSeeking() {
        Toast.makeText(mActivity, "Tracking stopped", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Tracking stopped");

        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationStoppedSeeking", "");
    }

    @Override
    public void onLocationFound(Location location) {
        Toast.makeText(mActivity, "Location found", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "Found location > Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());

        for (GeoLocation geoLocation : mData.getList()) {
            if (geoLocation.isUnlock()) {
                Log.d("LOCATION", geoLocation.getName() + " is already unlocked");
                continue;
            }

            float distance = geoLocation.toLocation().distanceTo(location);
            Log.d("LOCATION", "Distance between to " + geoLocation.getName() + " : " + distance);

            if (distance <= geoLocation.getRange()) {
                Log.d("LOCATION", geoLocation.getName() + " is in range");

                geoLocation.setUnlock(true);
            } else {
                Log.d("LOCATION", geoLocation.getName() + " is not in range");
            }
        }

        saveData();

        String rawData = mGson.toJson(mData);
        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnLocationFound", rawData);
    }

    @Override
    public void onGPSProviderDisabled() {
        Toast.makeText(mActivity, "GPS disabled", Toast.LENGTH_SHORT).show();
        Log.d("LOCATION", "GPS is disabled");

        UnityPlayer.UnitySendMessage("LOCATIONCHECKER", "OnGPSProviderDisabled", "");
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

    private String getDummyLocations() {
        GeoLocation locationFirst = new GeoLocation();
        locationFirst.setId(0);
        locationFirst.setName("Point 1");
        locationFirst.setRange(1000);
        locationFirst.setUnlock(false);
        locationFirst.setLatitude(40);
        locationFirst.setLongitude(40);

        GeoLocation locationSecond = new GeoLocation();
        locationSecond.setId(1);
        locationSecond.setName("Point 2");
        locationSecond.setRange(5000);
        locationSecond.setUnlock(true);
        locationSecond.setLatitude(50);
        locationSecond.setLongitude(50);

        ArrayList<GeoLocation> list = new ArrayList<>();
        list.add(locationFirst);
        list.add(locationSecond);

        GeoLocationList geoLocationList = new GeoLocationList();
        geoLocationList.setList(list);

        return mGson.toJson(geoLocationList);
    }
}
