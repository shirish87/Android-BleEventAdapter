package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;


public class DescriptorReadEvent extends GattEvent {
    public final BluetoothGattDescriptor desc;

    public DescriptorReadEvent(BluetoothGatt gatt, BluetoothGattDescriptor desc, int status) {
        super(gatt, status);
        this.desc = desc;
    }

}
