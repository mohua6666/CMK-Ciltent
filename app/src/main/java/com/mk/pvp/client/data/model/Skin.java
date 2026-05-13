package com.mk.pvp.client.data.model;

public class Skin {
    private String id;
    private String name;
    private String path;
    private String url;
    private String type;
    private boolean hasCape;
    private boolean isCustom;
    private long createdAt;

    public Skin() {}

    public Skin(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.id = System.currentTimeMillis() + "";
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean hasCape() {
        return hasCape;
    }

    public void setHasCape(boolean hasCape) {
        this.hasCape = hasCape;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
