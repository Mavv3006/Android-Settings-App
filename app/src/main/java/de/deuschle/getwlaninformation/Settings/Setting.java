package de.deuschle.getwlaninformation.Settings;

public abstract class Setting {

    int activeState;
    private String name;

    public int getActiveState() {
        return activeState;
    }

    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public abstract void activateActiveState();

    abstract int getState();

    abstract void next();

    abstract boolean setState(int state);
}
