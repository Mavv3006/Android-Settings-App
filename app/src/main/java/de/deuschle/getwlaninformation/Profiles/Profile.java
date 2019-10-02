package de.deuschle.getwlaninformation.Profiles;

import android.util.Log;

import java.util.Collection;
import java.util.Hashtable;

import de.deuschle.getwlaninformation.Settings.Setting;

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
        Log.d("ProfileActivation", "activate: Profile " + getName() + " stated");
        settingList.forEach(Setting::activateActiveState);
        Log.d("ProfileActivation", "activate: Profile " + getName() + " activated");
    }
}
