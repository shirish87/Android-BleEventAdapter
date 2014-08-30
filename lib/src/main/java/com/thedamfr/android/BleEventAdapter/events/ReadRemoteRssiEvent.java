package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;


public class ReadRemoteRssiEvent extends GattEvent {
    public final int rssi;

    public ReadRemoteRssiEvent(BluetoothGatt gatt, int rssi, int status) {
        super(gatt, status);
        this.rssi = rssi;
    }
}
