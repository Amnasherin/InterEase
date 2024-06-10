package com.example.InterEaseApp;

public class MenuItem {
    private String text;
    private int iconResId;

    public MenuItem(String text, int iconResId) {
        this.text = text;
        this.iconResId = iconResId;
    }

    public String getText() {
        return text;
    }

    public int getIconResId() {
        return iconResId;
    }
}

