package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


public class CharacteristicReadEvent extends GattEvent {
    public final BluetoothGattCharacteristic characteristic;

    public CharacteristicReadEvent(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super(gatt, status);
        this.characteristic = characteristic;
    }
}
