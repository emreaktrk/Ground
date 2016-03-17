package akturk.geochecker.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

@SuppressWarnings("MissingPermission")
public final class LocationProvider {

    private LocationManager mLocationManager;
    private SingleLocationListener mListener;
    private Location mLastKnownLocation;

    private OnLocationProviderListener mCallback;

    public LocationProvider(Context context) {
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

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Integer.MAX_VALUE, Integer.MAX_VALUE, mListener);
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

    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }

    private class SingleLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                mLastKnownLocation = location;

                if (mCallback != null) {
                    mCallback.onLocationFound(location);
                }

                stopSeeking();
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
