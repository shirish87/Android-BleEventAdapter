package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;


public class GattConnectionStateChangedEvent extends GattEvent {
    public final int newState;

    public GattConnectionStateChangedEvent(BluetoothGatt gatt, int status, int gattConnectionState) {
        super(gatt, status);
        newState = gattConnectionState;
    }

    @Override
    public String toString() {
        return "GattConnectionStateChangedEvent{" +
                "mGatt=" + gatt +
                ", mStatus=" + status +
                ", mNewState=" + newState +
                '}';
    }
}
