package akturk.geochecker;

import android.app.Activity;
import android.os.Bundle;

import com.akturk.plugin.LocationChecker;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationChecker checker = new LocationChecker(this);
        checker.start();
    }
}
