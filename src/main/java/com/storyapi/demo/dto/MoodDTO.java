package com.storyapi.demo.dto;

public class MoodDTO {
    private Long id;
    private String moodName;
    private String colorCode;
    
    public MoodDTO() {
    }
    
    public MoodDTO(Long id, String moodName, String colorCode) {
        this.id = id;
        this.moodName = moodName;
        this.colorCode = colorCode;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    public Long getId() {
        return id;
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
    
}
