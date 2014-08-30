package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;


public class DescriptorWriteEvent extends GattEvent {
    public final BluetoothGattDescriptor descriptor;

    public DescriptorWriteEvent(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super(gatt, status);
        this.descriptor = descriptor;
    }
}
