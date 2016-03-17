package akturk.geochecker.global;

import android.app.Application;
import android.content.Intent;

import akturk.geochecker.unity.bridge.BridgeService;

public final class GroundApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, BridgeService.class);
        startService(intent);
    }
}
