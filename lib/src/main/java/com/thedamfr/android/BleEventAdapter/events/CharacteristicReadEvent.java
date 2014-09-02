package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


public class CharacteristicReadEvent extends GattEvent {
    public final BluetoothGattCharacteristic chr;

    public CharacteristicReadEvent(BluetoothGatt gatt, BluetoothGattCharacteristic chr, int status) {
        super(gatt, status);
        this.chr = chr;
    }
}
