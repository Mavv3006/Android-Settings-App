package de.deuschle.getwlaninformation.Settings;

import android.bluetooth.BluetoothAdapter;

// Additional Infos: https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=en

public class Bluetooth extends TwoStyleSetting {

    private final BluetoothAdapter bluetoothAdapter;

    public Bluetooth(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.setName("bluetooth");
    }

    @Override
    public void turnOn() {
        bluetoothAdapter.enable();
    }

    @Override
    public void turnOff() {
        bluetoothAdapter.disable();
    }

    @Override
    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public String toString() {
        return Boolean.toString(isEnabled());
    }

    @Override
    public void setState(boolean state) {
        if (state) {
            turnOn();
        } else {
            turnOff();
        }
    }

    @Override
    public int getState() {
        return bluetoothAdapter.getState();
    }

    @Override
    public void next() {
        if (isEnabled()) {
            setState(false);
        } else {
            setState(true);
        }
    }

    @Override
    boolean setState(int state) {
        if (state == 10) {
            this.turnOff();
            return true;
        } else if (state == 12) {
            this.turnOn();
            return true;
        } else {
            return false;
        }
    }
}
