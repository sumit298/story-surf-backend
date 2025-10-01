package com.storyapi.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storyapi.demo.Service.AuthorApplicationService;
import com.storyapi.demo.config.CustomUserDetailsService;
import com.storyapi.demo.config.ResponseUtil;
import com.storyapi.demo.dto.AuthorApplicationCreateDTO;
import com.storyapi.demo.dto.AuthorApplicationDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/author-applications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthorApplicationController {
    
    @Autowired
    private AuthorApplicationService applicationService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitApplication(
            @Valid @RequestBody AuthorApplicationCreateDTO createDTO,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            
            AuthorApplicationDTO application = applicationService.submitApplication(createDTO, principal.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.success(application, "Application submitted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/my-applications")
    public ResponseEntity<Map<String, Object>> getMyApplications(Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            
            List<AuthorApplicationDTO> applications = applicationService.getUserApplications(principal.getId());
            
            Map<String, Object> response = Map.of(
                "applications", applications,
                "total", applications.size()
            );
            
            return ResponseEntity.ok(ResponseUtil.success(response, "Applications retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getApplicationById(@PathVariable Long id) {
        try {
            AuthorApplicationDTO application = applicationService.getApplicationById(id);
            return ResponseEntity.ok(ResponseUtil.success(application, "Application retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPendingApplications() {
        try {
            List<AuthorApplicationDTO> applications = applicationService.getPendingApplications();
            
            Map<String, Object> response = Map.of(
                "applications", applications,
                "total", applications.size()
            );
            
            return ResponseEntity.ok(ResponseUtil.success(response, "Pending applications retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @PutMapping("/admin/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> approveApplication(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            
            AuthorApplicationDTO application = applicationService.approveApplication(id, principal.getId());
            
            return ResponseEntity.ok(ResponseUtil.success(application, "Application approved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @PutMapping("/admin/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> rejectApplication(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            
            AuthorApplicationDTO application = applicationService.rejectApplication(id, principal.getId());
            
            return ResponseEntity.ok(ResponseUtil.success(application, "Application rejected"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getApplicationStats() {
        try {
            AuthorApplicationService.ApplicationStats stats = applicationService.getApplicationStats();
            
            return ResponseEntity.ok(ResponseUtil.success(stats, "Application statistics retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }
}