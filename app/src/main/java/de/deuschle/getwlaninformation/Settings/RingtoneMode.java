package de.deuschle.getwlaninformation.Settings;

import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Arrays;

public class RingtoneMode implements ThreeTypeSetting {

    private static RingtoneMode instance;
    private static ArrayList<String> audioStyle;
    private AudioManager audioManager;

    private RingtoneMode(AudioManager audiomanager) {
        this.audioManager = audiomanager;
        audioStyle = new ArrayList<>(Arrays.asList("Silent", "Vibrate", "Normal"));
    }

    public static RingtoneMode getInstance(AudioManager audioManager) {
        if (instance == null) {
            instance = new RingtoneMode(audioManager);
        }
        return instance;
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
