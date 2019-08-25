package de.deuschle.getwlaninformation.Settings;

import android.net.wifi.WifiManager;

// Additional Infos: https://developer.android.com/reference/android/net/wifi/WifiManager?hl=en

public class Wlan extends TwoStyleSetting {

    private final WifiManager wifiManager;

    public Wlan(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        this.setName("wlan");
    }

    @Override
    public void turnOn() {
        setState(true);
    }

    @Override
    public void turnOff() {
        setState(false);
    }

    @Override
    public boolean isEnabled() {
        return wifiManager.isWifiEnabled();
    }

    @Override
    boolean setState(int state) {
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            this.setState(false);
            return true;
        } else if (state == WifiManager.WIFI_STATE_ENABLED) {
            this.setState(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void activateActiveState() {
        this.setState(activeState);
    }

    @Override
    public int getState() {
        return wifiManager.getWifiState();
    }

    @Override
    void setState(boolean state) {
        wifiManager.setWifiEnabled(state);
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
    public String toString() {
        return Boolean.toString(isEnabled());
    }
}
