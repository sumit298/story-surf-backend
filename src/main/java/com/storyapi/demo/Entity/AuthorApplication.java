package com.storyapi.demo.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "AuthorApplication")
public class AuthorApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public void approve(User reviewer){
        this.status = ApplicationStatus.APPROVED;
        this.reviewedBy = reviewer;
        this.user.setRole(UserRole.AUTHOR);
    }
    
    public void reject(User reviewer){
        this.status = ApplicationStatus.REJECTED;
        this.reviewedBy = reviewer;
    }
    
    public AuthorApplication(){}
    
    public AuthorApplication(User user, String reason) {
        this.user = user;
        this.reason = reason;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getReason() {
        return reason;
    }
    
    public User getReviewedBy() {
        return reviewedBy;
    }
    public ApplicationStatus getStatus() {
        return status;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public User getUser() {
        return user;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public long getId() {
        return id;
    }
    
    
    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}