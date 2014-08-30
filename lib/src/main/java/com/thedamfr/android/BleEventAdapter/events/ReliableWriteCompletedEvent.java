package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;


public class ReliableWriteCompletedEvent extends GattEvent {

    public ReliableWriteCompletedEvent(BluetoothGatt gatt, int status) {
        super(gatt, status);
    }
}
