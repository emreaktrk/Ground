package com.akturk.plugin.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

@SuppressWarnings("MissingPermission")
public final class GPSManager {

    private LocationManager mLocationManager;
    private SingleLocationListener mListener;

    private OnLocationProviderListener mCallback;

    public GPSManager(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mListener = new SingleLocationListener();
    }

    public void startSeeking() {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mCallback != null) {
                mCallback.onGPSProviderDisabled();
                return;
            }
        }

        if (mCallback != null) {
            mCallback.onLocationStartedSeeking();
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mListener);
    }

    public void stopSeeking() {
        mLocationManager.removeUpdates(mListener);

        if (mCallback != null) {
            mCallback.onLocationStoppedSeeking();
        }
    }

    public void setOnLocationProviderListener(OnLocationProviderListener callback) {
        mCallback = callback;
    }


    private class SingleLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }

            if (mCallback != null) {
                mCallback.onLocationFound(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Empty Block
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Empty Block
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Empty Block
        }
    }


    public interface OnLocationProviderListener {
        void onLocationStartedSeeking();

        void onLocationStoppedSeeking();

        void onLocationFound(Location location);

        void onGPSProviderDisabled();
    }

}
