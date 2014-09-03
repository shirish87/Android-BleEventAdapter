package com.thedamfr.android.BleEventAdapter.service.gatt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.thedamfr.android.BleEventAdapter.BleEventAdapter;

public class GattService extends Service {
    private final static String TAG = GattService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    private String mBluetoothDeviceAddress;
    private int mConnectionState = BluetoothProfile.STATE_DISCONNECTED;

    private Handler uiHandler;

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
                final String address = intent.getStringExtra(BleEventAdapter.ADDRESS);

                if (address != null) {
                    //http://stackoverflow.com/a/20507449/1131910

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            connect(address);
                        }
                    });

                } else {
                    Log.e(TAG, "No address specified");
                }

            } else if (action.equals(BleEventAdapter.Actions.DISCONNECT)) {
                disconnect();
                stopSelf();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uiHandler = new Handler(getApplicationContext().getMainLooper());
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

        //-- following code does not work (status: 133), at least for LG L34C --
        //Previously connected device.  Try to reconnect.
        //if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
        //        && mBluetoothGatt != null) {
        //    Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
        //
        //    if (mBluetoothGatt.connect()) {
        //        mConnectionState = BluetoothProfile.STATE_CONNECTING;
        //        return true;
        //    } else {
        //        return false;
        //    }
        //}

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(getApplicationContext(), false, mGattCallBack);

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


    private BluetoothGattCallback mGattCallBack = new EventedGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            mConnectionState = newState;
        }
    };
}
