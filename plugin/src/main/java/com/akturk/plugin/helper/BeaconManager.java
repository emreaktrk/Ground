package com.akturk.plugin.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;

import com.akturk.plugin.model.Beacon;
import com.akturk.plugin.model.Target;

import java.util.ArrayList;

@SuppressWarnings("MissingPermission")
public final class BeaconManager extends BluetoothGattCallback implements BluetoothAdapter.LeScanCallback {

    private BluetoothAdapter mBluetoothAdapter;
    private OnBeaconProviderListener mCallback;

    private boolean mScanning;

    private ArrayList<Target> mList;

    public BeaconManager(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mList = new ArrayList<>();
    }

    public void startSeeking() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            if (mCallback != null) {
                mCallback.onBluetoothDisabled();
                return;
            }
        }

        mBluetoothAdapter.startLeScan(this);
        mScanning = true;

        if (mCallback != null) {
            mCallback.onBeaconStartedSeeking();
        }
    }

    public void stopSeeking() {
        mBluetoothAdapter.stopLeScan(this);
        mScanning = false;

        if (mCallback != null) {
            mCallback.onBeaconStoppedSeeking();
        }
    }

    public void setOnBeaconProviderListener(OnBeaconProviderListener callback) {
        this.mCallback = callback;
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        for (Target target : mList) {
            for (Beacon beacon : target.getBeacons()) {
                if (TextUtils.equals(beacon.getAdress(), bluetoothDevice.getAddress())) {
                    if (mCallback != null) {
                        mCallback.onBeaconFound(bluetoothDevice, target);
                        mList.remove(target);
                    }
                }
            }
        }
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void addTarget(Target object) {
        mList.add(object);
    }


    public interface OnBeaconProviderListener {
        void onBeaconStartedSeeking();

        void onBeaconStoppedSeeking();

        void onBluetoothDisabled();

        void onBeaconFound(BluetoothDevice device, Target target);
    }


}
