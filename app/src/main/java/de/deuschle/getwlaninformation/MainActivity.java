package de.deuschle.getwlaninformation;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import de.deuschle.getwlaninformation.Settings.*;

public class MainActivity extends AppCompatActivity {

    public Wlan wlan;
    public RingtoneMode ringtoneMode;
    public Bluetooth bluetooth;
    public NotificationManager notificationManager;

    private String profileName;
    private Boolean profileActive;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusView.setText(createStatusString());
                Snackbar.make(view, "Hier könnte ihre Werbung stehen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOn.setEnabled(false);
                buttonOff.setEnabled(true);
                wlan.turnOn();
                profileActive = false;
            }
        });
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOff.setEnabled(false);
                buttonOn.setEnabled(true);
                wlan.turnOff();
                profileActive = false;
            }
        });
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateProfile1();
                profileActive = true;
            }
        });
        profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateProfile2();
                profileActive = true;
            }
        });
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth.next();
                profileActive = false;
            }
        });
        ringtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtoneMode.next();
                profileActive = false;
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assert notificationManager != null;
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
            }
        }

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wlan = Wlan.getInstance(wifiManager);
        final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        ringtoneMode = RingtoneMode.getInstance(audioManager);
        final BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        assert bluetoothManager != null;
        final BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        bluetooth = Bluetooth.getInstance(bluetoothAdapter);
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
}
