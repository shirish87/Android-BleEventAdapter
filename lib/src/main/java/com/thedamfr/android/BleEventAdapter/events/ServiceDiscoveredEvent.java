package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;

public class ServiceDiscoveredEvent extends GattEvent {

    public ServiceDiscoveredEvent(BluetoothGatt gatt, int status) {
        super(gatt, status);
    }

    @Override
    public String toString() {
        return "ServiceDiscoveredEvent{" +
                "mStatus=" + status +
                ", mGatt=" + gatt +
                '}';
    }
}
