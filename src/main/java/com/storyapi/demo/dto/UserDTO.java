package com.storyapi.demo.dto;

import java.time.LocalDateTime;

import com.storyapi.demo.Entity.UserDirectory.User.UserRole;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
    private UserRole role;
    private String bio;
    private Boolean appliedForAuthor;
    private LocalDateTime createdAt;

    public UserDTO() {}

    public UserDTO(Long id, String name, String email, int age, 
                   UserRole role, String bio, Boolean appliedForAuthor, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.role = role;
        this.bio = bio;
        this.appliedForAuthor = appliedForAuthor;
        this.createdAt = createdAt;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getAppliedForAuthor() { return appliedForAuthor; }
    public void setAppliedForAuthor(Boolean appliedForAuthor) { this.appliedForAuthor = appliedForAuthor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}