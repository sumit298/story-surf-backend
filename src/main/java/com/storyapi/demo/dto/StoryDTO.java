package com.storyapi.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.StoryDirectory.Story.StoryLength;
import com.storyapi.demo.Entity.StoryDirectory.Story.StoryStatus;

public class StoryDTO {
    private Long id;
    private String title;
    private String content;
    private UserDTO author;
    private List<String> tags;
    private StoryLength contextTime;
    private StoryStatus status;
    private Long views;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reflectionCount;
    
    public StoryDTO() {
    }   
    
    public StoryDTO(Long id, String title, String content, UserDTO author, List<String> tags, StoryLength contextTime, StoryStatus status, Long views, Long likes, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
        this.contextTime = contextTime;
        this.status = status;
        this.views = views;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public UserDTO getAuthor() {
        return author;
    }
    
    public String getContent() {
        return content;
    }
    
    public StoryLength getContextTime() {
        return contextTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getLikes() {
        return likes;
    }
    
    public Long getReflectionCount() {
        return reflectionCount;
    }
    
    public StoryStatus getStatus() {
        return status;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public String getTitle() {
        return title;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public Long getViews() {
        return views;
    }
    
    public void setAuthor(UserDTO author) {
        this.author = author;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setContextTime(StoryLength contextTime) {
        this.contextTime = contextTime;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setLikes(Long likes) {
        this.likes = likes;
    }
    
    public void setReflectionCount(Long reflectionCount) {
        this.reflectionCount = reflectionCount;
    }
    
    public void setStatus(StoryStatus status) {
        this.status = status;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setViews(Long views) {
        this.views = views;
    }
}
