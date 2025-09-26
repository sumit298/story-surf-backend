package com.storyapi.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReflectionCreateDTO {
    @NotNull(message = "story id is required")
    private Long StoryId;
    
    @NotBlank(message = "type is required")
    private String type;
    
    private String content; 
    
    private String moodReaction;
    
    public ReflectionCreateDTO(){}
    
    public ReflectionCreateDTO(Long StoryId, String type, String content, String moodReaction){
        this.StoryId = StoryId;
        this.type = type;
        this.content = content;
        this.moodReaction = moodReaction;
    }   
    
    public String getContent() {
        return content;
    }
    
    public String getMoodReaction() {
        return moodReaction;
    }
    
    public Long getStoryId() {
        return StoryId;
    }
    
    public String getType() {
        return type;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setMoodReaction(String moodReaction) {
        this.moodReaction = moodReaction;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    
}
