package de.deuschle.getwlaninformation.Settings;

import android.app.NotificationManager;

import java.util.ArrayList;
import java.util.Arrays;

public class InterruptionFilter extends Setting {

    // None: Gar nicht stören
    // Priority: Nur mit Priorität
    // All: Ausgeschaltet
    private static final ArrayList<String> interruptionFilter = new ArrayList<>(Arrays.asList("Unknown", "All", "Priority", "None", "Alarms"));
    private final NotificationManager notificationManager;

    public InterruptionFilter(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.setName("interruptionFilter");
    }

    @Override
    int getState() {
        return notificationManager.getCurrentInterruptionFilter();
    }

    @Override
    public void next() {
        int newMode = getState() - 1 < 0 ? 4 : getState() - 1;
        setState(newMode);
    }

    @Override
    public String toString() {
        return getState() + " - " + interruptionFilter.get(getState());
    }

    @Override
    boolean setState(int state) {
        if (state >= 0 && state <= 4) {
            notificationManager.setInterruptionFilter(state);
            return true;
        } else {
            return false;
        }
    }
}
