package com.storyapi.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "mood")
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mood name is required")
    @Column(nullable = false, unique = true, length = 50)
    private String moodName;

    @NotBlank(message = "Color code is required")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code must be a valid hex code")
    @Column(name = "color_code", nullable = false, length = 7)
    private String colorCode;

    public Mood() {
    }

    public Mood(Long id, String moodName, String colorCode) {
        this.id = id;
        this.moodName = moodName;
        this.colorCode = colorCode;
    }

    public Long getId() {
        return id;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    @Override
    public String toString() {
        return "Mood [id=" + id + ", moodName=" + moodName + ", colorCode=" + colorCode + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Mood))
            return false;
        Mood mood = (Mood) o;
        return moodName != null && moodName.equals(mood.moodName);
    }

    @Override
    public int hashCode() {
        return moodName != null ? moodName.hashCode() : 0;
    }
}
