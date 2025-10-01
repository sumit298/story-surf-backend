package com.storyapi.demo.Entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.UserDirectory.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reflections")
public class Reflection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    @NotNull
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReflectionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_id")
    private Mood mood;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "mood_reaction")
    private String moodReaction;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public Reflection() {
    }

    public Reflection(Story story, User user, ReflectionType type, String content, String moodReaction) {
        this.story = story;
        this.user = user;
        this.type = type;
        this.content = content;
        this.moodReaction = moodReaction;
    }

    public Reflection(Story story, User user, ReflectionType type, String content) {
        this.story = story;
        this.user = user;
        this.type = type;
        this.content = content;
    }

    public void setMoodWithSync(Mood mood) {
        this.mood = mood;
        this.moodReaction = mood != null ? mood.getMoodName() : null;
    }

    /**
     * Check if this reflection has a mood component
     */
    public boolean hasMood() {
        return mood != null || (moodReaction != null && !moodReaction.trim().isEmpty());
    }

    /**
     * Get the effective mood name (prioritizes Mood entity over string)
     */
    public String getEffectiveMoodName() {
        return mood != null ? mood.getMoodName() : moodReaction;
    }

    /**
     * Check if this is a pure reaction (no comment text)
     */
    public boolean isPureReaction() {
        return type == ReflectionType.REACTION &&
                (content == null || content.trim().isEmpty()) &&
                hasMood();
    }

    /**
     * Check if this is a comment with mood
     */
    public boolean isCommentWithMood() {
        return type == ReflectionType.COMMENT &&
                content != null && !content.trim().isEmpty() &&
                hasMood();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setMoodReaction(String moodReaction) {
        this.moodReaction = moodReaction;
    }

    public String getMoodReaction() {
        return moodReaction;
    }

    public void setType(ReflectionType type) {
        this.type = type;
    }

    public ReflectionType getType() {
        return type;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Reflection))
            return false;
        Reflection that = (Reflection) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum ReflectionType {
        COMMENT,
        REACTION
    }

}
