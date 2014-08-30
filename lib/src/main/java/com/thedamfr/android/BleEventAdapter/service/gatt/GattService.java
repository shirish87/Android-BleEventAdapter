package com.thedamfr.android.BleEventAdapter.service.gatt;

import android.app.Service;
import android.bluetooth.*;
import android.content.Intent;
import android.os.IBinder;

import com.squareup.otto.Bus;
import com.thedamfr.android.BleEventAdapter.BleEventAdapter;
import com.thedamfr.android.BleEventAdapter.BleEventBusProvider;
import com.thedamfr.android.BleEventAdapter.events.*;

public class GattService extends Service {

    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mDevice;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDevice = BleEventAdapter.getInstance().getBluetoothDevice();
        if (mBluetoothGatt == null || mBluetoothGatt.connect())
            mBluetoothGatt = mDevice.connectGatt(this, false, mGattCallBack);


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.

        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }


    }

    private BluetoothGattCallback mGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            Bus bleEventBus = BleEventBusProvider.getBus();
            bleEventBus.post(new GattConnectionStateChangedEvent(mBluetoothGatt, status, newState));

            if (newState == GattConnectionState.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();
                bleEventBus.post(DiscoveryServiceEvent.GATT_DISCOVERING);
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                bleEventBus.post(DiscoveryServiceEvent.GATT_DISCONNECTED);
                mBluetoothGatt = mDevice.connectGatt(GattService.this, false, mGattCallBack);
            }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BleEventBusProvider.getBus()
                    .post(new ServiceDiscoveredEvent(mBluetoothGatt, status));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            BleEventBusProvider.getBus()
                    .post(new CharacteristicReadEvent(mBluetoothGatt, characteristic, status));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            BleEventBusProvider.getBus()
                    .post(new CharacteristicWriteEvent(mBluetoothGatt, characteristic, status));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            BleEventBusProvider.getBus()
                    .post(new CharacteristicChangedEvent(mBluetoothGatt, characteristic));
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            BleEventBusProvider.getBus()
                    .post(new DescriptorReadEvent(mBluetoothGatt, descriptor, status));
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            BleEventBusProvider.getBus()
                    .post(new DescriptorWriteEvent(mBluetoothGatt, descriptor, status));
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            BleEventBusProvider.getBus()
                    .post(new ReliableWriteCompletedEvent(mBluetoothGatt, status));

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            BleEventBusProvider.getBus()
                    .post(new ReadRemoteRssiEvent(mBluetoothGatt, rssi, status));
        }
    };


}
