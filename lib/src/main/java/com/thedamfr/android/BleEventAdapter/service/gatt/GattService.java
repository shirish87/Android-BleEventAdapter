package com.thedamfr.android.BleEventAdapter.service.gatt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Bus;
import com.thedamfr.android.BleEventAdapter.BleEventAdapter;
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

public class GattService extends Service {
    private final static String TAG = GattService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    private String mBluetoothDeviceAddress;
    private int mConnectionState = BluetoothProfile.STATE_DISCONNECTED;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (action == null) {
            Log.e(TAG, "No action specified");
        } else if (initialize()) {
            if (action.equals(BleEventAdapter.Actions.CONNECT)) {
                String address = intent.getStringExtra(BleEventAdapter.ADDRESS);

                if (address != null) {
                    connect(address);
                } else {
                    Log.e(TAG, "No address specified");
                }

            } else if (action.equals(BleEventAdapter.Actions.DISCONNECT)) {
                disconnect();
            }
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }


    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
                return false;
            }
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = BluetoothProfile.STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallBack);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = BluetoothProfile.STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.disconnect();
    }


    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
    

    private BluetoothGattCallback mGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            Bus bleEventBus = BleEventBusProvider.getBus();
            bleEventBus.post(new GattConnectionStateChangedEvent(gatt, status, newState));

            mConnectionState = newState;

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
    };


}
