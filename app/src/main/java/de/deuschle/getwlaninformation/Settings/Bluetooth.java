package de.deuschle.getwlaninformation.Settings;

import android.bluetooth.BluetoothAdapter;

public class Bluetooth extends TwoStyleSetting {

    private static Bluetooth instance;
    private BluetoothAdapter bluetoothAdapter;

    private Bluetooth(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public static Bluetooth getInstance(BluetoothAdapter bluetoothAdapter) {
        if (instance == null) {
            instance = new Bluetooth(bluetoothAdapter);
        }
        return instance;
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
}
