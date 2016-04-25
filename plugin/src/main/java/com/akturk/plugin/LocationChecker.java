package com.akturk.plugin;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.location.Location;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.akturk.plugin.adapter.TargetListAdapter;
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
    private boolean mAdapt;

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
        Log.d("LOCATION", "Tracking started");

        if (mAdapt) {
            TargetListAdapter adaptList = mGson.fromJson(mRawData, TargetListAdapter.class);
            mData = adaptList.adapt();
        } else {
            mData = mGson.fromJson(mRawData, TargetList.class);
        }

        UnityPlayer.UnitySendMessage("Bridge", "OnLocationStartedSeeking", "");
    }

    @Override
    public void onLocationStoppedSeeking() {
        Log.d("LOCATION", "Tracking stopped");

        UnityPlayer.UnitySendMessage("Bridge", "OnLocationStoppedSeeking", "");
    }

    @Override
    public void onLocationFound(Location location) {
        Log.d("LOCATION", "Found location > Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());

        UnityPlayer.UnitySendMessage("Bridge", "OnGPSFound", location.getLatitude() + "-" + location.getLongitude());

        for (Target target : mData.getList()) {
            if (target.isUnlock()) {
                Log.d("LOCATION", target.getName() + " is already unlocked");
                continue;
            }

            float distance = target.toLocation().distanceTo(location);
            Log.d("LOCATION", "Distance between to " + target.getName() + " : " + distance);

            if (distance <= target.getRangeAsMeters()) {
                Log.d("LOCATION", target.getName() + " is in range");

                if (target.hasBeacon()) {
                    if (!mBeaconManager.isScanning()) {
                        mBeaconManager.startSeeking();
                    }

                    mBeaconManager.addTargetIfNotExist(target);
                } else {
                    target.setUnlock(true);

                    if (mInBackground) {
                        saveData(target.getId() + "");

                        int iconId = mActivity.getResources().getIdentifier("notification_icon", "drawable", mActivity.getPackageName());
                        if (iconId == 0) {
                            return;
                        }

                        NotificationFacade notificationFacade = new NotificationFacade(mActivity);
                        notificationFacade
                                .getBuilder()
                                .setContentTitle("Target Found")
                                .setContentText("You have unlocked a target.")
                                .setSmallIcon(iconId)
                                .setAutoCancel(true);
                        notificationFacade.show();
                    } else {
                        UnityPlayer.UnitySendMessage("Bridge", "OnLocationFound", target.getId() + "");
                    }
                }

            } else {
                Log.d("LOCATION", target.getName() + " is not in range");
            }
        }
    }

    @Override
    public void onGPSProviderDisabled() {
        Log.d("LOCATION", "GPS is disabled");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start(mRawData);
            }
        }, 5000);

        if (mInBackground) {
            if (!OnetimeController.mGPS.isNotificationShown) {
                int iconId = mActivity.getResources().getIdentifier("notification_icon", "drawable", mActivity.getPackageName());
                if (iconId == 0) {
                    return;
                }

                NotificationFacade notificationFacade = new NotificationFacade(mActivity);
                notificationFacade
                        .getBuilder()
                        .setContentTitle("Time Looper")
                        .setContentText("Please turn your phone's location on.")
                        .setSmallIcon(iconId)
                        .setAutoCancel(true);
                notificationFacade.show();

                OnetimeController.mGPS.isNotificationShown = true;
            }
        } else {
            if (!OnetimeController.mGPS.isDialogShown) {
                UnityPlayer.UnitySendMessage("Bridge", "OnGPSProviderDisabled", "");

                OnetimeController.mGPS.isDialogShown = true;
            }
        }
    }

    @Override
    public void onBeaconStartedSeeking() {
        Log.d("BEACON", "Beacon started");
    }

    @Override
    public void onBeaconStoppedSeeking() {
        Log.d("BEACON", "Beacon stopped");
    }

    @Override
    public void onBluetoothDisabled() {
        Log.d("BEACON", "Bluetooth is disabled");

        if (mInBackground) {
            if (!OnetimeController.mBluetooth.isNotificationShown) {
                int iconId = mActivity.getResources().getIdentifier("notification_icon", "drawable", mActivity.getPackageName());
                if (iconId == 0) {
                    return;
                }

                NotificationFacade notificationFacade = new NotificationFacade(mActivity);
                notificationFacade
                        .getBuilder()
                        .setContentTitle("Time Looper")
                        .setContentText("Please turn your phone's bluetooth on.")
                        .setSmallIcon(iconId)
                        .setAutoCancel(true);
                notificationFacade.show();

                OnetimeController.mBluetooth.isNotificationShown = true;
            }
        } else {
            if (!OnetimeController.mBluetooth.isDialogShown) {
                UnityPlayer.UnitySendMessage("Bridge", "OnBluetoothDisabled", "");

                OnetimeController.mBluetooth.isDialogShown = true;
            }
        }
    }

    @Override
    public void onBeaconFound(BluetoothDevice device, Target target) {
        Log.d("BEACON", "Beacon found");

        int position = mData.getList().indexOf(target);
        mData.getList().get(position).setUnlock(true);

        if (mInBackground) {
            saveData(target.getId() + "");

            int iconId = mActivity.getResources().getIdentifier("notification_icon", "drawable", mActivity.getPackageName());
            if (iconId == 0) {
                return;
            }

            NotificationFacade notificationFacade = new NotificationFacade(mActivity);
            notificationFacade
                    .getBuilder()
                    .setContentTitle("Time Looper")
                    .setContentText("You can now watch " + target.getName() + " video.")
                    .setSmallIcon(iconId)
                    .setAutoCancel(true);
            notificationFacade.show();
        } else {
            UnityPlayer.UnitySendMessage("Bridge", "OnLocationFound", target.getId() + "");
        }


    }

    private void saveData(String id) {
        String data = readData();
        String content = TextUtils.isEmpty(data) ? id : (data + "-" + id);

        SharedPreferenceEngine
                .getInstance(mActivity)
                .edit()
                .putString(SharedPreferenceEngine.LOCATIONS, content)
                .apply();
    }

    private String readData() {
        return SharedPreferenceEngine
                .getInstance(mActivity)
                .getString(SharedPreferenceEngine.LOCATIONS, "");
    }

    public String returnData() {
        String data = SharedPreferenceEngine
                .getInstance(mActivity)
                .getString(SharedPreferenceEngine.LOCATIONS, "");

        flushData();

        return data;
    }

    private void flushData() {
        SharedPreferenceEngine
                .getInstance(mActivity)
                .edit()
                .putString(SharedPreferenceEngine.LOCATIONS, "")
                .apply();
    }

    public boolean isInBackground() {
        return mInBackground;
    }

    public void setInBackground(boolean background) {
        this.mInBackground = background;
    }

    public boolean isAdapt() {
        return mAdapt;
    }

    public void setAdapt(boolean adapt) {
        mAdapt = adapt;
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
        beacon.setAdress("760D2862-F66A-4AC5-A2D1-3E303C32030E");
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
