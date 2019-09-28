package de.deuschle.getwlaninformation.Settings;

import android.media.AudioManager;

public class Volume extends Setting {

    private AudioManager audioManager;
    private int streamType;

    Volume(AudioManager audioManager, int streamType) {
        this.audioManager = audioManager;
        this.streamType = streamType;
    }

    @Override
    int getState() {
        return audioManager.getStreamVolume(streamType);
    }

    @Override
    public void next() {
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        int currentVolume = getState();
        if (currentVolume < maxVolume) {
            setState(++currentVolume);
        }
    }

    @Override
    boolean setState(int state) {
        audioManager.setStreamVolume(streamType, state, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        return true;
    }
}
