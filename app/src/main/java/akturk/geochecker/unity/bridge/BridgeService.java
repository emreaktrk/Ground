package akturk.geochecker.unity.bridge;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import akturk.geochecker.helper.LocationProvider;

public final class BridgeService extends Service implements LocationProvider.OnLocationProviderListener {

    private ServiceBinder mBinder;
    private LocationProvider mLocationProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationProvider = new LocationProvider(this);
        mLocationProvider.setOnLocationProviderListener(this);
        mLocationProvider.startSeeking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocationProvider.stopSeeking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new ServiceBinder();
        }

        return mBinder;
    }

    @Override
    public void onLocationStartedSeeking() {
        Log.d("LOCATION", "Started Seeking");
        Toast.makeText(BridgeService.this, "Started Seeking", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationStoppedSeeking() {
        Log.d("LOCATION", "Stoped Seeking");
        Toast.makeText(BridgeService.this, "Stoped Seeking", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationFound(Location location) {
        Log.d("LOCATION", "Location Found");
        Toast.makeText(BridgeService.this, "Location Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSProviderDisabled() {
        Log.d("LOCATION", "GPS Disabled");
        Toast.makeText(BridgeService.this, "GPS Disabled", Toast.LENGTH_SHORT).show();
    }

    public class ServiceBinder extends Binder {

        BridgeService getService() {
            return BridgeService.this;
        }
    }
}
