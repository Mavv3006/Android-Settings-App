package de.deuschle.getwlaninformation.Profiles;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import de.deuschle.getwlaninformation.Settings.Setting;

public class Profile {

    private String name;
    private Hashtable<String, Setting> settingDictionary;
    private Collection<Setting> settingList;

    public Profile(String name, Hashtable<String, Setting> dictionary) {
        this.settingDictionary = dictionary;
        this.name = name;
        this.settingList = dictionary.values();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addToDictionary(String key, Setting value) {
        this.settingDictionary.put(key, value);
    }

    public Dictionary<String, Setting> getSettingDictionary() {
        return settingDictionary;
    }

    public void activate() {
        settingList.forEach(Setting::activateActiveState);
    }
}
