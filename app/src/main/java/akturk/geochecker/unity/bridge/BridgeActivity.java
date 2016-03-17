package akturk.geochecker.unity.bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

public final class BridgeActivity extends UnityPlayerActivity {

    private LocationServiceConnection mConnection;
    private BridgeService mService;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mConnection = new LocationServiceConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopTracking();
    }

    public void startTracking() {
        Intent intent = new Intent(this, BridgeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopTracking() {
        unbindService(mConnection);
    }

    private class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BridgeService.ServiceBinder binder = (BridgeService.ServiceBinder) service;
            mService = binder.getService();

            Log.d("UNITY", "Service connected: " + mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("UNITY", "Service disconnected: " + mService);
        }
    }
}
