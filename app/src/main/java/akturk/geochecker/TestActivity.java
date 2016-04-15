package akturk.geochecker;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.akturk.plugin.LocationChecker;

public class TestActivity extends Activity {

    private String mRawData = "{\"Data\":[{\"altitude\":100,\"id\":0,\"latitude\":40.98572136342678,\"longitude\":28.842766577540512,\"name\":\"Point 1\",\"range\":1000,\"unlock\":false},{\"altitude\":0,\"beacons\":[{\"adress\":\"0C:F3:EE:00:7D:9A\",\"major\":9,\"minor\":1,\"range\":0,\"uuid\":\"760D2862-F66A-4AC5-A2D1-3E303C32030E\"}],\"id\":1,\"latitude\":40.98572136342678,\"longitude\":28.842766577540512,\"name\":\"Point 2\",\"range\":5000,\"unlock\":false}]}";
    private LocationChecker mLocationChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationChecker = new LocationChecker(this);
        mLocationChecker.start(mRawData);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocationChecker.setInBackground(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocationChecker.setInBackground(true);
    }
}
