package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


public class CharacteristicWriteEvent extends GattEvent {
    public final BluetoothGattCharacteristic chr;

    public CharacteristicWriteEvent(BluetoothGatt gatt, BluetoothGattCharacteristic chr, int status) {
        super(gatt, status);
        this.chr = chr;
    }

    @Override
    public String toString() {
        return "CharacteristicWriteEvent{" +
                "mGatt=" + gatt +
                ", mCharacteristic=" + chr +
                ", mStatus=" + status +
                '}';
    }
}
