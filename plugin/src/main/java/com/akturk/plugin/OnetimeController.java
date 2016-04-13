package com.akturk.plugin;

final class OnetimeController {

    static GPS mGPS = new GPS();
    static BLUETOOTH mBluetooth = new BLUETOOTH();


    static class GPS {
        boolean isPushSent;
        boolean isDialogShown;
    }

    static class BLUETOOTH {
        boolean isPushSent;
        boolean isDialogShown;
    }

}
