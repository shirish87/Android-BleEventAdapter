package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public class CharacteristicChangedEvent extends GattEvent {
    public final BluetoothGattCharacteristic characteristic;

    public CharacteristicChangedEvent(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super(gatt, BluetoothGatt.GATT_SUCCESS);
        this.characteristic = characteristic;
    }

    @Override
    public String toString() {
        return "CharacteristicWriteEvent{" +
                "mGatt=" + gatt +
                ", mCharacteristic=" + characteristic +
                '}';
    }
}
