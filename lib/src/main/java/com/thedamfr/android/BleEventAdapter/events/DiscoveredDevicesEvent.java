package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothDevice;

import java.util.Arrays;
import java.util.Set;

public class DiscoveredDevicesEvent {

    private final BluetoothDevice[] bluetoothDevices;
    private final BluetoothDevice device;

    public DiscoveredDevicesEvent(BluetoothDevice device, Set<BluetoothDevice> bluetoothDevices) {
        this.device = device;
        this.bluetoothDevices = bluetoothDevices.toArray(new BluetoothDevice[bluetoothDevices.size()]);
    }

    public BluetoothDevice[] getBluetoothDevices() {
        return bluetoothDevices;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "DiscoveredDevicesEvent{" +
                "bluetoothDevices=" + Arrays.toString(bluetoothDevices) +
                '}';
    }
}
