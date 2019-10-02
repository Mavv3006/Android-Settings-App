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

    // State 0 not reachable without INTERRUPTION_FILTER_NONE !
    @Override
    public void next() {
        if (getState() == 0) {
            return;
        }
        setState(getState() == 1 ? 2 : 1);
    }

    @Override
    boolean setState(int state) {
        switch (state) {
            case 1:
            case 2:
                audioManager.setRingerMode(state);
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return getState() + " - " + audioStyle.get(getState());
    }
}

