package com.mk.pvp.client.data.model;

public class PVPSettings {
    private boolean showFPS;
    private boolean showPing;
    private boolean autoJump;
    private boolean fastPlace;
    private boolean fastBreak;
    private float reachDistance;
    private float clickSpeed;
    private boolean keymacroEnabled;
    private String keymacroKey;
    private int keymacroDelay;
    private boolean hitboxDisplay;
    private boolean armorHUD;
    private boolean crosshairStyle;
    private int crosshairSize;

    public PVPSettings() {
        this.showFPS = true;
        this.showPing = true;
        this.autoJump = false;
        this.fastPlace = false;
        this.fastBreak = false;
        this.reachDistance = 4.0f;
        this.clickSpeed = 10.0f;
        this.keymacroEnabled = false;
        this.keymacroKey = "NONE";
        this.keymacroDelay = 50;
        this.hitboxDisplay = false;
        this.armorHUD = true;
        this.crosshairStyle = true;
        this.crosshairSize = 1;
    }

    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }

    public boolean isShowPing() {
        return showPing;
    }

    public void setShowPing(boolean showPing) {
        this.showPing = showPing;
    }

    public boolean isAutoJump() {
        return autoJump;
    }

    public void setAutoJump(boolean autoJump) {
        this.autoJump = autoJump;
    }

    public boolean isFastPlace() {
        return fastPlace;
    }

    public void setFastPlace(boolean fastPlace) {
        this.fastPlace = fastPlace;
    }

    public boolean isFastBreak() {
        return fastBreak;
    }

    public void setFastBreak(boolean fastBreak) {
        this.fastBreak = fastBreak;
    }

    public float getReachDistance() {
        return reachDistance;
    }

    public void setReachDistance(float reachDistance) {
        this.reachDistance = reachDistance;
    }

    public float getClickSpeed() {
        return clickSpeed;
    }

    public void setClickSpeed(float clickSpeed) {
        this.clickSpeed = clickSpeed;
    }

    public boolean isKeymacroEnabled() {
        return keymacroEnabled;
    }

    public void setKeymacroEnabled(boolean keymacroEnabled) {
        this.keymacroEnabled = keymacroEnabled;
    }

    public String getKeymacroKey() {
        return keymacroKey;
    }

    public void setKeymacroKey(String keymacroKey) {
        this.keymacroKey = keymacroKey;
    }

    public int getKeymacroDelay() {
        return keymacroDelay;
    }

    public void setKeymacroDelay(int keymacroDelay) {
        this.keymacroDelay = keymacroDelay;
    }

    public boolean isHitboxDisplay() {
        return hitboxDisplay;
    }

    public void setHitboxDisplay(boolean hitboxDisplay) {
        this.hitboxDisplay = hitboxDisplay;
    }

    public boolean isArmorHUD() {
        return armorHUD;
    }

    public void setArmorHUD(boolean armorHUD) {
        this.armorHUD = armorHUD;
    }

    public boolean isCrosshairStyle() {
        return crosshairStyle;
    }

    public void setCrosshairStyle(boolean crosshairStyle) {
        this.crosshairStyle = crosshairStyle;
    }

    public int getCrosshairSize() {
        return crosshairSize;
    }

    public void setCrosshairSize(int crosshairSize) {
        this.crosshairSize = crosshairSize;
    }
}
