package com.akturk.plugin;

final class OnetimeController {

    static GPS mGPS = new GPS();
    static BLUETOOTH mBluetooth = new BLUETOOTH();


    static class GPS {
        boolean isNotificationShown;
        boolean isDialogShown;
    }

    static class BLUETOOTH {
        boolean isNotificationShown;
        boolean isDialogShown;
    }

}
