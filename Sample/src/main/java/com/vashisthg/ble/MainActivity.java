package com.vashisthg.ble;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.thedamfr.android.BleEventAdapter.BleEventAdapter;
import com.thedamfr.android.BleEventAdapter.BleEventBusProvider;
import com.thedamfr.android.BleEventAdapter.events.DiscoveredDevicesEvent;

/**
 * Created by vashisthg on 14/05/14.
 */
public class MainActivity extends Activity {

    private static final String LOGTAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BleEventBusProvider.getBus().register(this);
        BleEventAdapter.startScanning(this);
    }

    @Subscribe
    public void onDiscoveredDevice(DiscoveredDevicesEvent event){
        BluetoothDevice device = event.getDevice();

        if(device != null) {
            Log.d(LOGTAG, "discovered device" + device.getName());
        }
    }

    @Override
    protected void onPause() {

        BleEventAdapter.stopScanning(this);
        BleEventBusProvider.getBus().unregister(this);
        super.onPause();
    }
}
