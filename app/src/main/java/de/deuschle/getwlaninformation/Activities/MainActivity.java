package de.deuschle.getwlaninformation.Activities;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Hashtable;
import java.util.Objects;

import de.deuschle.getwlaninformation.Profiles.Profile;
import de.deuschle.getwlaninformation.R;
import de.deuschle.getwlaninformation.Settings.Bluetooth;
import de.deuschle.getwlaninformation.Settings.InterruptionFilter;
import de.deuschle.getwlaninformation.Settings.RingtoneMode;
import de.deuschle.getwlaninformation.Settings.Setting;
import de.deuschle.getwlaninformation.Settings.Wlan;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private AudioManager audioManager;
    private BluetoothAdapter bluetoothAdapter;
    private NotificationManager notificationManager;

    private Wlan wlan;
    private RingtoneMode ringtoneMode;
    private Bluetooth bluetooth;
    private InterruptionFilter doNotDisturb;

    private Hashtable<String, Profile> profileDictionary = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        initiateSettingVariables();
        createProfiles();

        FloatingActionButton fab = findViewById(R.id.fab);
        TextView textView = findViewById(R.id.statusTextView);
        RadioButton profieWork = findViewById(R.id.profileWork);
        RadioButton profileHome = findViewById(R.id.profileHome);
        RadioButton profileTravel = findViewById(R.id.profileTravel);

        fab.setOnClickListener(view -> {
            textView.setText(createStatusString());
            Snackbar.make(view, "Hier könnte ihre Werbung stehen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        profieWork.setOnClickListener(view -> activateProfile("work"));
        profileHome.setOnClickListener(view -> activateProfile("home"));
        profileTravel.setOnClickListener(view -> activateProfile("travel"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initiateSettingVariables() {
        assert notificationManager != null;
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }

        wlan = new Wlan(wifiManager);
        ringtoneMode = new RingtoneMode(audioManager);
        bluetooth = new Bluetooth(bluetoothAdapter);
        doNotDisturb = new InterruptionFilter(notificationManager);
    }

    private String createStatusString() {
        String result = "Wlan: " + wlan.toString();
        result += "\nRingtone: " + ringtoneMode.toString();
        result += "\nBluetooth: " + bluetooth.toString();
        result += "\nInterruptionFilter: " + doNotDisturb.toString();
        return result;
    }

    private void activateProfile(String name) {
        Profile profile = profileDictionary.get(name);
        if (profile != null) {
            String profileName = profile.getName();
            profile.activate();
        }
    }

    void createProfiles() {
        Hashtable<String, Setting> homeDic = createProfile(WifiManager.WIFI_STATE_ENABLED, AudioManager.RINGER_MODE_NORMAL, BluetoothAdapter.STATE_ON, NotificationManager.INTERRUPTION_FILTER_ALL);
        Hashtable<String, Setting> workDic = createProfile(WifiManager.WIFI_STATE_ENABLED, AudioManager.RINGER_MODE_SILENT, BluetoothAdapter.STATE_OFF, NotificationManager.INTERRUPTION_FILTER_PRIORITY);
        Hashtable<String, Setting> travelDic = createProfile(WifiManager.WIFI_STATE_DISABLED, AudioManager.RINGER_MODE_VIBRATE, BluetoothAdapter.STATE_ON, NotificationManager.INTERRUPTION_FILTER_ALL);

        Profile home = new Profile("home", homeDic);
        Profile work = new Profile("work", workDic);
        Profile travel = new Profile("travel", travelDic);

        profileDictionary.put(home.getName(), home);
        profileDictionary.put(work.getName(), work);
        profileDictionary.put(travel.getName(), travel);
    }

    Hashtable<String, Setting> createProfile(int wifiState, int ringerState, int bluetoothState, int interruptionState) {
        Hashtable<String, Setting> settingHashtable = new Hashtable<>();

        Wlan wlan = new Wlan(wifiManager);
        RingtoneMode ringtoneMode = new RingtoneMode(audioManager);
        Bluetooth bluetooth = new Bluetooth(bluetoothAdapter);
        InterruptionFilter interruptionFilter = new InterruptionFilter(notificationManager);

        wlan.setActiveState(wifiState);
        ringtoneMode.setActiveState(ringerState);
        bluetooth.setActiveState(bluetoothState);
        interruptionFilter.setActiveState(interruptionState);

        settingHashtable.put(wlan.getName(), wlan);
        settingHashtable.put(ringtoneMode.getName(), ringtoneMode);
        settingHashtable.put(bluetooth.getName(), bluetooth);
        settingHashtable.put(interruptionFilter.getName(), interruptionFilter);

        return settingHashtable;
    }
}
