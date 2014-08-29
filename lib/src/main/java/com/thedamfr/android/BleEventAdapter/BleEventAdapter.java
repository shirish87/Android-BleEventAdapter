package com.thedamfr.android.BleEventAdapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import com.thedamfr.android.BleEventAdapter.service.discovery.device.DeviceDiscoveryService;
import com.thedamfr.android.BleEventAdapter.service.gatt.GattService;

public enum BleEventAdapter {
    INSTANCE;

    private BluetoothDevice bluetoothDevice;

    public static BleEventAdapter getInstance() {
        return INSTANCE;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    private void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public static void connectDevice(Context context, BluetoothDevice device) {
        context.stopService(new Intent(context, GattService.class));
    }

    public static void closeConnection(Context context) {
        context.stopService(new Intent(context, GattService.class));
    }

    public static void startScanning(Context context) {
        context.startService(new Intent(context, DeviceDiscoveryService.class));
    }

    public static void stopScanning(Context context) {
        context.stopService(new Intent(context, DeviceDiscoveryService.class));
    }
}
