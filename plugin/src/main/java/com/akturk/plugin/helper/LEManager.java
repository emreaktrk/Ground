package com.akturk.plugin.helper;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.akturk.plugin.model.Beacon;
import com.akturk.plugin.model.Target;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressWarnings("MissingPermission")
public final class LEManager extends BluetoothGattCallback {

    private BluetoothAdapter mBluetoothAdapter;
    private LEScanCallback mScanCallback;
    private OnBeaconProviderListener mCallback;

    private boolean mScanning;

    private ArrayList<Target> mList;

    public LEManager(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mScanCallback = new LEScanCallback();

        mList = new ArrayList<>();
    }

    public void startSeeking() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            if (mCallback != null) {
                mCallback.onBluetoothDisabled();
                return;
            }
        }

        mBluetoothAdapter.getBluetoothLeScanner().startScan(mScanCallback);
        mScanning = true;

        if (mCallback != null) {
            mCallback.onBeaconStartedSeeking();
        }
    }

    public void stopSeeking() {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
        mScanning = false;

        if (mCallback != null) {
            mCallback.onBeaconStoppedSeeking();
        }
    }

    public void setOnBeaconProviderListener(OnBeaconProviderListener callback) {
        this.mCallback = callback;
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void addTargetIfNotExist(Target object) {
        if (mList.contains(object)) {
            return;
        }

        mList.add(object);
    }

    private void getSettings(){
        new ScanSettings
                .Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    private class LEScanCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            for (Target target : mList) {
                for (Beacon beacon : target.getBeacons()) {
                    if (TextUtils.equals(beacon.getAdress(), result.getDevice().getAddress())) {
                        if (mCallback != null) {
                            mCallback.onBeaconFound(result.getDevice(), target);
                            mList.remove(target);
                        }
                    }
                }
            }

        }
    }


    public interface OnBeaconProviderListener {
        void onBeaconStartedSeeking();

        void onBeaconStoppedSeeking();

        void onBluetoothDisabled();

        void onBeaconFound(BluetoothDevice device, Target target);
    }


}
