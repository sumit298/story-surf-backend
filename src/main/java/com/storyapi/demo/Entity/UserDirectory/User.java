package com.storyapi.demo.Entity.UserDirectory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import com.storyapi.demo.Entity.AuthorApplication;
import com.storyapi.demo.Entity.StoryDirectory.Story;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age should be atleast 18 years old.")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.READER;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "applied_for_author")
    private Boolean appliedForAuthor = false;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Embedded
    private UserSettings settings = new UserSettings();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_saved_stories", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "story_id"))
    private List<Story> savedStories = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Story> authorStories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AuthorApplication> authorApplications = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public User() {

    }

    public User(String name, String email, Integer age, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public List<Story> getAuthorStories() {
        return authorStories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;

        if (role == UserRole.AUTHOR) {
            this.appliedForAuthor = true;
        }
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getAppliedForAuthor() {
        return appliedForAuthor;
    }

    public void setAppliedForAuthor(Boolean appliedForAuthor) {
        this.appliedForAuthor = appliedForAuthor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public List<Story> getSavedStories() {
        return savedStories;
    }

    public void setSavedStories(List<Story> savedStories) {
        this.savedStories = savedStories;
    }

    public List<AuthorApplication> getAuthorApplications() {
        return authorApplications;
    }

    public void setAuthorApplications(List<AuthorApplication> authorApplications) {
        this.authorApplications = authorApplications;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public enum UserRole {
        READER, AUTHOR, ADMIN
    }
}
