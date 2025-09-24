package com.storyapi.demo.Entity.UserDirectory;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserSettings {
    private String fontSize;
    private String theme;

    public UserSettings() {
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}