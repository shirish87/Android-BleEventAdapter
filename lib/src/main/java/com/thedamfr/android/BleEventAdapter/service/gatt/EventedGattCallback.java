package com.thedamfr.android.BleEventAdapter.service.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;

import com.squareup.otto.Bus;
import com.thedamfr.android.BleEventAdapter.BleEventBusProvider;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicChangedEvent;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicReadEvent;
import com.thedamfr.android.BleEventAdapter.events.CharacteristicWriteEvent;
import com.thedamfr.android.BleEventAdapter.events.DescriptorReadEvent;
import com.thedamfr.android.BleEventAdapter.events.DescriptorWriteEvent;
import com.thedamfr.android.BleEventAdapter.events.DiscoveryServiceEvent;
import com.thedamfr.android.BleEventAdapter.events.GattConnectionStateChangedEvent;
import com.thedamfr.android.BleEventAdapter.events.ReadRemoteRssiEvent;
import com.thedamfr.android.BleEventAdapter.events.ReliableWriteCompletedEvent;
import com.thedamfr.android.BleEventAdapter.events.ServiceDiscoveredEvent;

public class EventedGattCallback extends BluetoothGattCallback {

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        Bus bleEventBus = BleEventBusProvider.getBus();
        bleEventBus.post(new GattConnectionStateChangedEvent(gatt, status, newState));

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            if (gatt.discoverServices()) {
                bleEventBus.post(DiscoveryServiceEvent.GATT_DISCOVERING);
            } else {
                // unlikely
                bleEventBus.post(DiscoveryServiceEvent.GATT_DISCOVER_FAILED);
            }

        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            bleEventBus.post(DiscoveryServiceEvent.GATT_DISCONNECTED);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        BleEventBusProvider.getBus()
                .post(new ServiceDiscoveredEvent(gatt, status));
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        BleEventBusProvider.getBus()
                .post(new CharacteristicReadEvent(gatt, characteristic, status));
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        BleEventBusProvider.getBus()
                .post(new CharacteristicWriteEvent(gatt, characteristic, status));
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        BleEventBusProvider.getBus()
                .post(new CharacteristicChangedEvent(gatt, characteristic));
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        BleEventBusProvider.getBus()
                .post(new DescriptorReadEvent(gatt, descriptor, status));
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        BleEventBusProvider.getBus()
                .post(new DescriptorWriteEvent(gatt, descriptor, status));
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        BleEventBusProvider.getBus()
                .post(new ReliableWriteCompletedEvent(gatt, status));

    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        BleEventBusProvider.getBus()
                .post(new ReadRemoteRssiEvent(gatt, rssi, status));
    }
}
