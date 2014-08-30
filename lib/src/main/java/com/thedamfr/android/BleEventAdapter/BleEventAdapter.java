package com.thedamfr.android.BleEventAdapter;


import android.content.Context;
import android.content.Intent;

import com.thedamfr.android.BleEventAdapter.service.discovery.device.DeviceDiscoveryService;
import com.thedamfr.android.BleEventAdapter.service.gatt.GattService;

public enum BleEventAdapter {
    INSTANCE;

    public static final String ADDRESS = "address";

    public interface Actions {
        String CONNECT = "connect";
        String DISCONNECT = "disconnect";
    }

    public static void connectDevice(Context context, String deviceAddress) {
        Intent intent = new Intent(context, GattService.class);
        intent.setAction(Actions.CONNECT);
        intent.putExtra(ADDRESS, deviceAddress);
        context.startService(intent);
    }


    public static void disconnectDevice(Context context) {
        Intent intent = new Intent(context, GattService.class);
        intent.setAction(Actions.DISCONNECT);
        context.startService(intent);
    }

    public static void startScanning(Context context) {
        context.startService(new Intent(context, DeviceDiscoveryService.class));
    }

    public static void stopScanning(Context context) {
        context.stopService(new Intent(context, DeviceDiscoveryService.class));
    }
}
