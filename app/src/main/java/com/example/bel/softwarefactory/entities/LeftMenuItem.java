package com.example.bel.softwarefactory.entities;

public class LeftMenuItem {
    private String title;
    private String description;
    private int icon;
    private int id;

    public LeftMenuItem(String title, String description, int icon, int id) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

}

