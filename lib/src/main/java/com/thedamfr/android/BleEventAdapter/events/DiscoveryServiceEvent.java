package com.thedamfr.android.BleEventAdapter.events;


public enum DiscoveryServiceEvent {
    GATT_DISCOVERING, GATT_DISCONNECTED;

    @Override
    public String toString() {
        return "DiscoveryServiceEvent{" +
                "mStatus=" + this.name() +
                '}';
    }
}
