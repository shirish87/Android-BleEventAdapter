package com.thedamfr.android.BleEventAdapter.events;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.Arrays;
import java.util.Set;

public class DiscoveredDevicesEvent {
    private static final String TAG = DiscoveredDevicesEvent.class.getSimpleName();

    public final BluetoothDevice[] bluetoothDevices;

    public final BluetoothDevice device;
    public final int rssi;
    public final byte[] scanRecord;

    public DiscoveredDevicesEvent(BluetoothDevice device, int rssi, byte[] scanRecord,
                                  Set<BluetoothDevice> bluetoothDevices) {
        this.device = device;
        this.bluetoothDevices = bluetoothDevices.toArray(new BluetoothDevice[bluetoothDevices.size()]);
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    public BluetoothDevice getDeviceByName(String name) {
        for (final BluetoothDevice bled : bluetoothDevices) {
            String deviceName = bled.getName();
            Log.d(TAG, "Device found: " + deviceName + " @ " + bled.getAddress());

            if (deviceName != null && deviceName.equalsIgnoreCase(name)) {
                return bled;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return "DiscoveredDevicesEvent{" +
                "bluetoothDevices=" + Arrays.toString(bluetoothDevices) +
                '}';
    }
}
