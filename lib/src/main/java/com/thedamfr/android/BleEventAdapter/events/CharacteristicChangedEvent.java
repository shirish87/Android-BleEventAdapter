package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public class CharacteristicChangedEvent extends GattEvent {
    public final BluetoothGattCharacteristic chr;

    public CharacteristicChangedEvent(BluetoothGatt gatt, BluetoothGattCharacteristic chr) {
        super(gatt, BluetoothGatt.GATT_SUCCESS);
        this.chr = chr;
    }

    @Override
    public String toString() {
        return "CharacteristicWriteEvent{" +
                "mGatt=" + gatt +
                ", mCharacteristic=" + chr +
                '}';
    }
}
