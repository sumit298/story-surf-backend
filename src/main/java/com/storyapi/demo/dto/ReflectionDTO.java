package com.storyapi.demo.dto;

import java.time.LocalDateTime;

import com.storyapi.demo.Entity.Reflection.ReflectionType;

public class ReflectionDTO {
    private Long id;
    private Long storyId;
    private UserDTO user;
    private ReflectionType type;
    private String content;
    private String moodReaction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ReflectionDTO() {
    }
    
    public ReflectionDTO(Long id, Long storyId, UserDTO user, ReflectionType type, String content, String moodReaction,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storyId = storyId;
        this.user = user;
        this.type = type;
        this.content = content;
        this.moodReaction = moodReaction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getMoodReaction() {
        return moodReaction;
    }
    
    public Long getStoryId() {
        return storyId;
    }
    
    public ReflectionType getType() {
        return type;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setMoodReaction(String moodReaction) {
        this.moodReaction = moodReaction;
    }
    
    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }
    
    public void setType(ReflectionType type) {
        this.type = type;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
}
