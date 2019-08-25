package de.deuschle.getwlaninformation.Settings;

import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Arrays;

// Additional Infos: https://developer.android.com/reference/android/media/AudioManager

public class RingtoneMode extends ThreeTypeSetting {

    private static ArrayList<String> audioStyle;
    private final AudioManager audioManager;

    public RingtoneMode(AudioManager audiomanager) {
        this.audioManager = audiomanager;
        audioStyle = new ArrayList<>(Arrays.asList("Silent", "Vibrate", "Normal"));
        this.setName("rintone");
    }

    @Override
    public void activateActiveState() {
        this.setRinger(activeState);
    }

    public int getState() {
        return audioManager.getRingerMode();
    }

    @Override
    public void next() {
        int newMode = getState() - 1 < 0 ? 2 : getState() - 1;
        setRinger(newMode);
    }

    @Override
    boolean setState(int state) {
        if (state >= 0 && state <= 3) {
            this.setRinger(state);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTo0() { // silent
        setRinger(0);
    }

    @Override
    public void setTo1() { // vibrate
        setRinger(1);
    }

    @Override
    public void setTO2() { // normal
        setRinger(2);
    }

    @Override
    public String toString() {
        return audioStyle.get(getState());
    }

    private void setRinger(int mode) {
        audioManager.setRingerMode(mode);
    }
}
