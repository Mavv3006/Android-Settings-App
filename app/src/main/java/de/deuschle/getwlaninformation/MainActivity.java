package de.deuschle.getwlaninformation;

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
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

import de.deuschle.getwlaninformation.Profiles.Profile;
import de.deuschle.getwlaninformation.Settings.Bluetooth;
import de.deuschle.getwlaninformation.Settings.RingtoneMode;
import de.deuschle.getwlaninformation.Settings.Setting;
import de.deuschle.getwlaninformation.Settings.Wlan;

public class MainActivity extends AppCompatActivity {

    private final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    private final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    private final BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
    private final BluetoothAdapter bluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();

    private String profileName;
    private Boolean profileActive;
    private Wlan wlan;
    private RingtoneMode ringtoneMode;
    private Bluetooth bluetooth;
    private NotificationManager notificationManager;
    private Dictionary<String, Profile> profileDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiateSettingVariables();

        final Button buttonOn = findViewById(R.id.button2);
        final Button buttonOff = findViewById(R.id.button);
        final TextView statusView = findViewById(R.id.statusView);
        Button bluetoothButton = findViewById(R.id.bluetooth);
        Button ringtoneButton = findViewById(R.id.ringtoneMode);
        Button profile1 = findViewById(R.id.profile1);
        Button profile2 = findViewById(R.id.profile2);
        FloatingActionButton fab = findViewById(R.id.fab);

        if (wlan.isEnabled()) {
            buttonOn.setEnabled(false);
            buttonOff.setEnabled(true);
        } else {
            buttonOn.setEnabled(true);
            buttonOff.setEnabled(false);
        }

        profileActive = false;

        statusView.setText(createStatusString());

        fab.setOnClickListener(view -> {
            statusView.setText(createStatusString());
            Snackbar.make(view, "Hier könnte ihre Werbung stehen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        buttonOn.setOnClickListener(view -> {
            buttonOn.setEnabled(false);
            buttonOff.setEnabled(true);
            wlan.turnOn();
            profileActive = false;
        });
        buttonOff.setOnClickListener(view -> {
            buttonOff.setEnabled(false);
            buttonOn.setEnabled(true);
            wlan.turnOff();
            profileActive = false;
        });
        profile1.setOnClickListener(view -> {
            activateProfile1();
            profileActive = true;
        });
        profile2.setOnClickListener(view -> {
            activateProfile2();
            profileActive = true;
        });
        bluetoothButton.setOnClickListener(view -> {
            bluetooth.next();
            profileActive = false;
        });
        ringtoneButton.setOnClickListener(view -> {
            ringtoneMode.next();
            profileActive = false;
        });
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
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }

        wlan = new Wlan(wifiManager);
        ringtoneMode = new RingtoneMode(audioManager);
        assert bluetoothManager != null;
        bluetooth = new Bluetooth(bluetoothAdapter);
    }

    private String createStatusString() {
        String result = "Wlan: " + wlan.toString();
        result += "\nRingtone: " + ringtoneMode.toString();
        result += "\nBluetooth: " + bluetooth.toString();
        if (profileActive) {
            result += "\nProfile: " + profileName;
        }
        return result;
    }

    private void activateProfile1() {
        profileName = "Home";
        wlan.turnOn();
        bluetooth.turnOn();
        ringtoneMode.setTO2();
    }

    private void activateProfile2() {
        profileName = "Work";
        wlan.turnOn();
        bluetooth.turnOff();
        ringtoneMode.setTo0();
    }

    void createProfiles() {
        Hashtable<String, Setting> homeDic = new Hashtable<>();
        Hashtable<String, Setting> workDic = new Hashtable<>();

        Wlan wlanHome = new Wlan(wifiManager);
        wlanHome.setActiveState(WifiManager.WIFI_STATE_ENABLED);
        RingtoneMode ringtoneHome = new RingtoneMode(audioManager);
        ringtoneHome.setActiveState(AudioManager.RINGER_MODE_NORMAL);
        Bluetooth bluetoothHome = new Bluetooth(bluetoothAdapter);
        bluetoothHome.setActiveState(BluetoothAdapter.STATE_ON);

        homeDic.put(wlanHome.getName(), wlanHome);
        homeDic.put(ringtoneHome.getName(), ringtoneHome);
        homeDic.put(bluetoothHome.getName(), bluetoothHome);

        Wlan wlanWork = new Wlan(wifiManager);
        wlanWork.setActiveState(WifiManager.WIFI_STATE_ENABLED);
        RingtoneMode ringtoneWork = new RingtoneMode(audioManager);
        ringtoneWork.setActiveState(AudioManager.RINGER_MODE_SILENT);
        Bluetooth bluetoothWork = new Bluetooth(bluetoothAdapter);
        bluetoothWork.setActiveState(BluetoothAdapter.STATE_OFF);

        workDic.put(wlanWork.getName(), wlanWork);
        workDic.put(ringtoneWork.getName(), ringtoneWork);
        workDic.put(bluetoothWork.getName(), bluetoothWork);

        Profile home = new Profile("home", homeDic);
        Profile work = new Profile("work", workDic);

        profileDictionary.put(home.getName(), home);
        profileDictionary.put(work.getName(), work);
    }
}
