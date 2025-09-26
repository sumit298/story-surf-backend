package com.storyapi.demo.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class StoryCreateDTO {
    @NotBlank(message = "title is required")
    private String title;   
    
    @NotBlank(message = "content is required")
    private String content;
    
    private List<String> tags;
    
    @NotBlank(message = "contextTime is required")
    private String contextTime; 
    
    public StoryCreateDTO(){}
    
    public StoryCreateDTO(String title, String content, List<String> tags, String contextTime){
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.contextTime = contextTime;
    }
    
    public String getContent() {
        return content;
    }

    public String getContextTime() {
        return contextTime;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setContextTime(String contextTime) {
        this.contextTime = contextTime;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
