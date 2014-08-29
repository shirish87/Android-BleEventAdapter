package com.thedamfr.android.BleEventAdapter;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


public enum BleEventBusProvider {
    INSTANCE;

    private final Bus bleEventBus;

    private BleEventBusProvider() {
        bleEventBus = new Bus(ThreadEnforcer.ANY);
    }

    public Bus getBleEventBus() {
        return bleEventBus;
    }

    public static Bus getBus() {
        return INSTANCE.getBleEventBus();
    }
}
