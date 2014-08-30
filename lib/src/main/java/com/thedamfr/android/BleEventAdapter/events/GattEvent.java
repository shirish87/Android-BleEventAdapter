package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;

public abstract class GattEvent {
    public final BluetoothGatt gatt;
    public final int status;

    public GattEvent(BluetoothGatt gatt, int status) {
        this.gatt = gatt;
        this.status = status;
    }

    public boolean isSuccess() {
        return (BluetoothGatt.GATT_SUCCESS == status);
    }
}
