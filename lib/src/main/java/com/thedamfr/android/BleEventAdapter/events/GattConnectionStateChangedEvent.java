package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;


public class GattConnectionStateChangedEvent extends GattEvent {
    public boolean isConnected;
    public final int newState;

    public GattConnectionStateChangedEvent(BluetoothGatt gatt, int status, int gattConnectionState) {
        super(gatt, status);

        isConnected = (status == BluetoothProfile.STATE_CONNECTED);
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
