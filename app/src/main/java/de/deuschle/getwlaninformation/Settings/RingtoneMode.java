package de.deuschle.getwlaninformation.Settings;

import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Arrays;

// Additional Infos: https://developer.android.com/reference/android/media/AudioManager

public class RingtoneMode extends Setting {

    private static final ArrayList<String> audioStyle = new ArrayList<>(Arrays.asList("Silent", "Vibrate", "Normal"));
    private final AudioManager audioManager;

    public RingtoneMode(AudioManager audiomanager) {
        this.audioManager = audiomanager;
        this.setName("ringtone");
    }

    public int getState() {
        return audioManager.getRingerMode();
    }

    @Override
    public void next() {
        int newMode = getState() - 1 < 0 ? 2 : getState() - 1;
        setState(newMode);
    }

    @Override
    boolean setState(int state) {
        if (state >= 0 && state <= 3) {
            audioManager.setRingerMode(state);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return audioStyle.get(getState());
    }
}

