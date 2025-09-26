package com.storyapi.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorApplicationCreateDTO {
    @NotBlank(message = "reason is required")
    private String reason;  
    
    public AuthorApplicationCreateDTO(){}
    
    
    public AuthorApplicationCreateDTO(String reason) {
        this.reason = reason;
    }   
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
}
