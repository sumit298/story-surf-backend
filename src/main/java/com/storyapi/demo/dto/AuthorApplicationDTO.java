package com.storyapi.demo.dto;

import java.time.LocalDateTime;

import com.storyapi.demo.Entity.AuthorApplication.ApplicationStatus;

public class AuthorApplicationDTO {
    private Long id;
    private UserDTO user;
    private ApplicationStatus status;
    private String reason;
    private UserDTO reviewedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AuthorApplicationDTO() {
    }
    
    public AuthorApplicationDTO(Long id, UserDTO user, ApplicationStatus status, String reason, UserDTO reviewedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.reason = reason;
        this.reviewedBy = reviewedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }   
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getReason() {
        return reason;
    }
    
    public UserDTO getReviewedBy() {
        return reviewedBy;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public void setReviewedBy(UserDTO reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    
}
