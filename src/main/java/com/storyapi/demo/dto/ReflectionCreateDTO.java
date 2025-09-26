package com.storyapi.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReflectionCreateDTO {
    @NotNull
    private Long storyId;
    
    @NotNull
    private String content;
    
    private String moodReaction;
    
    public String getMoodReaction() {
        return moodReaction;
    }
    
    public void setMoodReaction(String moodReaction) {
        this.moodReaction = moodReaction;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    @NotBlank
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
