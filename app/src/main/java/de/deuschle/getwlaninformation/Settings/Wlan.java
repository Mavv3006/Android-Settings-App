package de.deuschle.getwlaninformation.Settings;

import android.net.wifi.WifiManager;

public class Wlan extends TwoStyleSetting {

    private static Wlan instance;
    private WifiManager wifiManager;

    private Wlan(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public static Wlan getInstance(WifiManager wifiManager) {
        if (instance == null) {
            instance = new Wlan(wifiManager);
        }
        return instance;
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
    public void setState(boolean state) {
        wifiManager.setWifiEnabled(state);
    }

    @Override
    public int getState() {
        return wifiManager.getWifiState();
    }

    @Override
    public void next() {
        if(isEnabled()){
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
