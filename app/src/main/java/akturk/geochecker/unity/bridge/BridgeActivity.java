package akturk.geochecker.unity.bridge;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import akturk.geochecker.R;
import akturk.geochecker.helper.LocationProvider;

public class BridgeActivity extends Activity implements LocationProvider.OnLocationProviderListener {

    private LocationProvider mLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bridge);

        mLocationProvider = new LocationProvider(this);
        mLocationProvider.setOnLocationProviderListener(this);
        mLocationProvider.startSeeking();
    }

    @Override
    public void onLocationStartedSeeking() {
        Toast.makeText(this, "onLocationStartedSeeking", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationStoppedSeeking() {
        Toast.makeText(this, "onLocationStoppedSeeking", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationFound(Location location) {
        Toast.makeText(this, "onLocationFound", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSProviderDisabled() {
        Toast.makeText(this, "onGPSProviderDisabled", Toast.LENGTH_SHORT).show();
    }
}
