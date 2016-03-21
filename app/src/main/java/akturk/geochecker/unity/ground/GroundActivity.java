package akturk.geochecker.unity.ground;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import akturk.geochecker.global.SharedData;
import akturk.geochecker.helper.LocationProvider;
import akturk.geochecker.helper.SharedPreferenceEngine;
import akturk.geochecker.model.GeoLocation;
import akturk.geochecker.model.GeoLocationList;

public final class GroundActivity extends Activity implements LocationProvider.OnLocationProviderListener {

    private LocationProvider mLocationProvider;

    private GeoLocationList mData;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGson = new Gson();

        SharedData.setMinTime(5000);
        SharedData.setMinDistance(0);

        mLocationProvider = new LocationProvider(this);
        mLocationProvider.setOnLocationProviderListener(this);
        mLocationProvider.startSeeking();
    }

    @Override
    public void onLocationStartedSeeking() {
        Log.d("LOCATION", "Tracking started");

        String rawData = SharedPreferenceEngine.getInstance(this).getString(SharedPreferenceEngine.LOCATIONS, getDummyLocations());
        mData = mGson.fromJson(rawData, GeoLocationList.class);
    }

    @Override
    public void onLocationStoppedSeeking() {
        Log.d("LOCATION", "Tracking stopped");
    }

    @Override
    public void onLocationFound(Location location) {
        Log.d("LOCATION", "Found location > Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());

        for (GeoLocation geoLocation : mData.getList()) {
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
    }

    @Override
    public void onGPSProviderDisabled() {
        Toast.makeText(this, "onGPSProviderDisabled", Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        String rawData = mGson.toJson(mData);
        SharedPreferenceEngine
                .getInstance(this)
                .edit()
                .putString(SharedPreferenceEngine.LOCATIONS, rawData)
                .apply();
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
