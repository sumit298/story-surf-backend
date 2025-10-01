package com.storyapi.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storyapi.demo.Entity.AuthorApplication;
import com.storyapi.demo.Entity.AuthorApplication.ApplicationStatus;
import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;
import com.storyapi.demo.Repository.AuthorApplicationRepository;
import com.storyapi.demo.Repository.UserRepository;
import com.storyapi.demo.dto.AuthorApplicationCreateDTO;
import com.storyapi.demo.dto.AuthorApplicationDTO;
import com.storyapi.demo.mapper.DTOMapper;

@Service
@Transactional
public class AuthorApplicationService {
    
    @Autowired
    private AuthorApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DTOMapper mapper;
    
    public AuthorApplicationDTO submitApplication(AuthorApplicationCreateDTO createDTO, Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != UserRole.READER) {
            throw new InvalidRequestException("Only readers can apply to become authors");
        }
        
        if (applicationRepository.existsByUserAndStatus(user, ApplicationStatus.PENDING)) {
            throw new InvalidRequestException("User already has a pending application");
        }
        
        AuthorApplication application = new AuthorApplication(user, createDTO.getReason());
        AuthorApplication savedApplication = applicationRepository.save(application);
        
        user.setAppliedForAuthor(true);
        userRepository.save(user);
        
        return mapper.toAuthorApplicationDTO(savedApplication);
    }
    
    @Transactional(readOnly = true)
    public List<AuthorApplicationDTO> getPendingApplications() {
        List<AuthorApplication> applications = applicationRepository.findByStatusOrderByCreatedAtAsc(ApplicationStatus.PENDING);
        return mapper.toAuthorApplicationDTOList(applications);
    }
    
    @Transactional(readOnly = true)
    public List<AuthorApplicationDTO> getUserApplications(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        List<AuthorApplication> applications = applicationRepository.findByUser(user);
        return mapper.toAuthorApplicationDTOList(applications);
    }
    
    public AuthorApplicationDTO approveApplication(Long applicationId, Long adminId) throws ResourceNotFoundException {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Only admins can approve applications");
        }
        
        AuthorApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new InvalidRequestException("Only pending applications can be approved");
        }
        
        application.approve(admin);
        AuthorApplication savedApplication = applicationRepository.save(application);
        userRepository.save(application.getUser());
        
        return mapper.toAuthorApplicationDTO(savedApplication);
    }
    
    public AuthorApplicationDTO rejectApplication(Long applicationId, Long adminId) throws ResourceNotFoundException {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Only admins can reject applications");
        }
        
        AuthorApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new InvalidRequestException("Only pending applications can be rejected");
        }
        
        application.reject(admin);
        AuthorApplication savedApplication = applicationRepository.save(application);
        
        return mapper.toAuthorApplicationDTO(savedApplication);
    }
    
    @Transactional(readOnly = true)
    public AuthorApplicationDTO getApplicationById(Long applicationId) throws ResourceNotFoundException {
        AuthorApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        return mapper.toAuthorApplicationDTO(application);
    }
    
    @Transactional(readOnly = true)
    public ApplicationStats getApplicationStats() {
        long totalApplications = applicationRepository.count();
        long pendingApplications = applicationRepository.countByStatus(ApplicationStatus.PENDING);
        long approvedApplications = applicationRepository.countByStatus(ApplicationStatus.APPROVED);
        long rejectedApplications = applicationRepository.countByStatus(ApplicationStatus.REJECTED);
        
        return new ApplicationStats(totalApplications, pendingApplications, approvedApplications, rejectedApplications);
    }
    
    public static class ApplicationStats {
        private final long totalApplications;
        private final long pendingApplications;
        private final long approvedApplications;
        private final long rejectedApplications;
        
        public ApplicationStats(long totalApplications, long pendingApplications, long approvedApplications, long rejectedApplications) {
            this.totalApplications = totalApplications;
            this.pendingApplications = pendingApplications;
            this.approvedApplications = approvedApplications;
            this.rejectedApplications = rejectedApplications;
        }
        
        public long getTotalApplications() { return totalApplications; }
        public long getPendingApplications() { return pendingApplications; }
        public long getApprovedApplications() { return approvedApplications; }
        public long getRejectedApplications() { return rejectedApplications; }
    }
}
