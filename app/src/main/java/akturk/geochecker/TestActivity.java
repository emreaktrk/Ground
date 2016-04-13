package akturk.geochecker;

import android.app.Activity;
import android.os.Bundle;

import com.akturk.plugin.LocationChecker;

public class TestActivity extends Activity {

    String mRawData = "{\"Data\":[{\"altitude\":100,\"id\":0,\"latitude\":40,\"longitude\":40,\"name\":\"Point 1\",\"range\":1000,\"unlock\":false},{\"altitude\":0,\"beacons\":[{\"adress\":\"beacon_mac_adressiniz\",\"major\":9,\"minor\":1,\"range\":0,\"uuid\":\"760D2862-F66A-4AC5-A2D1-3E303C32030E\"}],\"id\":1,\"latitude\":50,\"longitude\":50,\"name\":\"Point 2\",\"range\":5000,\"unlock\":true}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationChecker checker = new LocationChecker(this);
        checker.setAdapt(true);
        checker.start(mRawData);
    }
}
