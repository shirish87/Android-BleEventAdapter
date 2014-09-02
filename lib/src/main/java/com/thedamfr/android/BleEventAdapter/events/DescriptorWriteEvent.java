package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;


public class DescriptorWriteEvent extends GattEvent {
    public final BluetoothGattDescriptor desc;

    public DescriptorWriteEvent(BluetoothGatt gatt, BluetoothGattDescriptor desc, int status) {
        super(gatt, status);
        this.desc = desc;
    }
}
