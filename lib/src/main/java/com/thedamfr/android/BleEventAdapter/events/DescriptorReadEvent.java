package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;


public class DescriptorReadEvent extends GattEvent {
    public final BluetoothGattDescriptor descriptor;

    public DescriptorReadEvent(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super(gatt, status);
        this.descriptor = descriptor;
    }

}
