package de.deuschle.getwlaninformation.Profiles;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Objects;

import de.deuschle.getwlaninformation.Settings.Bluetooth;
import de.deuschle.getwlaninformation.Settings.InterruptionFilter;
import de.deuschle.getwlaninformation.Settings.RingtoneMode;
import de.deuschle.getwlaninformation.Settings.Setting;
import de.deuschle.getwlaninformation.Settings.Wlan;

public class Profile {

    private String name;
    private Collection<Setting> settingList;

    public Profile(String name, Hashtable<String, Setting> dictionary) {
        this.name = name;
        this.settingList = dictionary.values();
    }

    public String getName() {
        return name;
    }

    public void activate() {
        settingList.forEach(Setting::activateActiveState);
        Log.d("ProfileActivation", "activate: Profile " + getName() + " activated");
    }

    public static class ProfileBuilder {
        private Context context;
        private Hashtable<String, Setting> dictionary;

        public ProfileBuilder(Context context) {
            this.context = context;
            dictionary = new Hashtable<>();
        }

        public void setWlan(int state) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Wlan wlan = new Wlan(wifiManager);
            wlan.setActiveState(state);
            dictionary.put(wlan.getName(), wlan);
        }

        public void setBluetooth(int state) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();
            Bluetooth bluetooth = new Bluetooth(bluetoothAdapter);
            bluetooth.setActiveState(state);
            dictionary.put(bluetooth.getName(), bluetooth);
        }

        public void setRingtoneMode(int state) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            RingtoneMode ringtoneMode = new RingtoneMode(audioManager);
            ringtoneMode.setActiveState(state);
            dictionary.put(ringtoneMode.getName(), ringtoneMode);
        }

        public void setInterruptionFilter(int state) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            InterruptionFilter interruptionFilter = new InterruptionFilter(notificationManager);
            interruptionFilter.setActiveState(state);
            dictionary.put(interruptionFilter.getName(), interruptionFilter);
        }

        public Profile buildProfile(String name) {
            return new Profile(name, dictionary);
        }
    }
}
