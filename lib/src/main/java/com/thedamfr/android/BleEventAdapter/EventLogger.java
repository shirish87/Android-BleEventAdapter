package com.thedamfr.android.BleEventAdapter;


import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicChangedEvent;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicReadEvent;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicWriteEvent;
import com.thedamfr.android.BleEventAdapter.events.DescriptorReadEvent;
import com.thedamfr.android.BleEventAdapter.events.DescriptorWriteEvent;
import com.thedamfr.android.BleEventAdapter.events.DiscoveredDevicesEvent;
import com.thedamfr.android.BleEventAdapter.events.DiscoveryServiceEvent;
import com.thedamfr.android.BleEventAdapter.events.GattConnectionStateChangedEvent;
import com.thedamfr.android.BleEventAdapter.events.ReadRemoteRssiEvent;
import com.thedamfr.android.BleEventAdapter.events.ReliableWriteCompletedEvent;
import com.thedamfr.android.BleEventAdapter.events.ScanningEvent;
import com.thedamfr.android.BleEventAdapter.events.ServiceDiscoveredEvent;

import java.util.List;

public class EventLogger {

    private static final String TAG = "BLE_EVENT_BUS";

    public EventLogger() {
        BleEventBusProvider.getBus()
                .register(this);
    }

    @Subscribe
    public void logDeviceDiscovered(DiscoveredDevicesEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void logScanningEvent(ScanningEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(CharacteristicChangedEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(CharacteristicReadEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(CharacteristicWriteEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(DescriptorReadEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(DescriptorWriteEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(GattConnectionStateChangedEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(ReadRemoteRssiEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(ReliableWriteCompletedEvent event) {
        Log.i(TAG, event.toString());
    }

    @Subscribe
    public void log(ServiceDiscoveredEvent event) {
        Log.i(TAG, event.toString());
        List<BluetoothGattService> services = event.gatt.getServices();
        for(BluetoothGattService service : services)
        {
            Log.i(TAG, service.getClass().getSimpleName() + " : "
                    + service.getUuid().toString() + " "
                    + service.getCharacteristics().size() + " Characteristics" + " "
                    + service.getIncludedServices().size() + " Included Services");
        }
    }

    @Subscribe
    public void log(DiscoveryServiceEvent event) {
        Log.i(TAG, event.toString());
    }
}
