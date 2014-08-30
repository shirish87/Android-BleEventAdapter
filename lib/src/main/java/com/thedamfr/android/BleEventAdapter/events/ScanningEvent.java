package com.thedamfr.android.BleEventAdapter.events;


public class ScanningEvent {
    public final boolean scanning;

    public ScanningEvent(boolean scanning) {
        this.scanning = scanning;
    }

    @Override
    public String toString() {
        return "Scanning Event : " + scanning;
    }
}
