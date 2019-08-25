package de.deuschle.getwlaninformation.Settings;

abstract class TwoStyleSetting extends Setting {
    abstract void turnOn();

    abstract void turnOff();

    abstract boolean isEnabled();

    abstract void setState(boolean state);

    @Override
    public void next() {
        if (isEnabled()) {
            setState(false);
        } else {
            setState(true);
        }
    }
}
