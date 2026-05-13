package com.mk.pvp.client.data.model;

import java.util.List;

public class KeyMapping {
    private String name;
    private String keyCode;
    private String action;
    private String category;
    private int positionX;
    private int positionY;
    private int width;
    private int height;

    public KeyMapping() {}

    public KeyMapping(String name, String keyCode, String action) {
        this.name = name;
        this.keyCode = keyCode;
        this.action = action;
        this.category = "movement";
        this.width = 60;
        this.height = 60;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}