package com.thedamfr.android.BleEventAdapter.service.discovery.device;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.squareup.otto.Produce;
import com.thedamfr.android.BleEventAdapter.BleEventBusProvider;
import com.thedamfr.android.BleEventAdapter.events.BluetoothUnavailable;
import com.thedamfr.android.BleEventAdapter.events.DiscoveredDevicesEvent;
import com.thedamfr.android.BleEventAdapter.events.ScanningEvent;

import java.util.HashSet;
import java.util.Set;


public class DeviceDiscoveryService extends Service {

    private static final long SCAN_PERIOD = 10000;
    private Set<BluetoothDevice> mBluetoothDevices = new HashSet<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private boolean mScanning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BleEventBusProvider.getBus().register(this);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            BleEventBusProvider.getBus().post(new BluetoothUnavailable());
            stopSelf();
        } else {
            scanLeDevice(true);
        }

        return START_NOT_STICKY;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            startScanning();
        } else {
            stopScanning();
        }
    }

    private void startScanning() {
        // Stops scanning after a
        //... pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        }, SCAN_PERIOD);

        mScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        BleEventBusProvider.getBus()
                .post(new ScanningEvent(mScanning));
    }

    private void stopScanning() {
        mScanning = false;
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        BleEventBusProvider.getBus()
                .post(new ScanningEvent(false));
        DeviceDiscoveryService.this.stopSelf();
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    mBluetoothDevices.add(device);
                    BleEventBusProvider.getBus()
                            .post(new DiscoveredDevicesEvent(device, mBluetoothDevices));
                }
            };

    @Produce
    public DiscoveredDevicesEvent produceAnswer() {
        return new DiscoveredDevicesEvent(null, mBluetoothDevices);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScanning();
        BleEventBusProvider.getBus()
                .unregister(this);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
