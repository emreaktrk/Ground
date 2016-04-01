package akturk.geochecker;

import android.app.Activity;
import android.os.Bundle;

import com.akturk.plugin.LocationChecker;

public class TestActivity extends Activity {

    String mRawData = "{\"scenarios\":[{\"id\":0,\"latitude\":40,\"longitude\":40,\"name\":\"Point 1\",\"range\":1000,\"unlock\":false},{\"id\":1,\"latitude\":50,\"longitude\":50,\"name\":\"Point 2\",\"range\":5000,\"unlock\":true}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationChecker checker = new LocationChecker(this);
        checker.start(mRawData);
    }
}
