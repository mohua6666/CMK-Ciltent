package com.mk.pvp.client.data.model;

public class AppSettings {
    private String theme;
    private int maxMemory;
    private String renderer;
    private String selectedVersion;
    private float mouseSensitivity;
    private boolean autoLaunch;
    private boolean showConsole;
    private boolean enableSound;

    public AppSettings() {
        this.theme = "dark";
        this.maxMemory = 2048;
        this.renderer = "virgl";
        this.selectedVersion = "";
        this.mouseSensitivity = 1.0f;
        this.autoLaunch = false;
        this.showConsole = true;
        this.enableSound = true;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getSelectedVersion() {
        return selectedVersion;
    }

    public void setSelectedVersion(String selectedVersion) {
        this.selectedVersion = selectedVersion;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public boolean isAutoLaunch() {
        return autoLaunch;
    }

    public void setAutoLaunch(boolean autoLaunch) {
        this.autoLaunch = autoLaunch;
    }

    public boolean isShowConsole() {
        return showConsole;
    }

    public void setShowConsole(boolean showConsole) {
        this.showConsole = showConsole;
    }

    public boolean isEnableSound() {
        return enableSound;
    }

    public void setEnableSound(boolean enableSound) {
        this.enableSound = enableSound;
    }
}