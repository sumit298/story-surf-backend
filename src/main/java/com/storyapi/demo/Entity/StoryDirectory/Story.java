package com.storyapi.demo.Entity.StoryDirectory;

import com.storyapi.demo.Entity.Reflection;
import com.storyapi.demo.Entity.UserDirectory.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "story")
public class Story {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   
   @NotBlank(message = "title is required")
   @Column(nullable = false)
   private String title;
   
    @NotBlank(message = "Content is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "Author is required")
    private User author;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "story_tags", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoryLength contextTime;
    
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reflection> reflections = new ArrayList<>();
    
    @Column(nullable = false)
    private Long views = 0L;
    
    @Column(nullable = false)
    private Long likes = 0L;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoryStatus status = StoryStatus.DRAFT;
    
    @CreationTimestamp
    @Column(name="createdAt")
    private LocalDateTime createdAt;
    
    @CreationTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    
    public Story() {
    }
    
    public Story(String title, String content, User author, List<String> tags, StoryLength contextTime) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
        this.contextTime = contextTime;
    }
    
    public long getId() {
        return id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setReflections(List<Reflection> reflections) {
        this.reflections = reflections;
    }
    
    public List<Reflection> getReflections() {
        return reflections;
    }
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    public User getAuthor() {
        return author;
    }
    
    public void setContextTime(StoryLength contextTime) {
        this.contextTime = contextTime;
    }
    
    public StoryLength getContextTime() {
        return contextTime;
    }
    

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setLikes(Long likes) {
        this.likes = likes;
    }
    
    public Long getLikes() {
        return likes;
    }
    
    public void setStatus(StoryStatus status) {
        this.status = status;
    }
    
    public StoryStatus getStatus() {
        return status;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setViews(Long views) {
        this.views = views;
    }
    
    public Long getViews() {
        return views;
    }
    
    
    
    
    
}

enum StoryStatus {
   DRAFT,      // Author is still writing
    SUBMITTED,  // Submitted for review
    APPROVED,   // Admin approved
    PUBLISHED,  // Live on platform
    ARCHIVED    // Hidden/removed

}

enum StoryLength {
    SHORT, MEDIUM, LONG
}
