package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


public class CharacteristicWriteEvent extends GattEvent {
    public final BluetoothGattCharacteristic characteristic;

    public CharacteristicWriteEvent(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super(gatt, status);
        this.characteristic = characteristic;
    }

    @Override
    public String toString() {
        return "CharacteristicWriteEvent{" +
                "mGatt=" + gatt +
                ", mCharacteristic=" + characteristic +
                ", mStatus=" + status +
                '}';
    }
}
