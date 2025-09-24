package com.storyapi.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRegistrationDTO {
    @NotBlank(message = "name is required")
    private String name;
    
    @Email(message = "email should be valid")
    @NotBlank(message = "email is required")
    private String email;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age should be equal or greater than 18")
    private Integer age;
    
    @NotBlank(message = "password is required")
    private String password;
    
    
    private String bio;
    
    public  UserRegistrationDTO(){}
    
    public UserRegistrationDTO(String name, String email, Integer age, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getBio() {
        return bio;
    }
    
}
