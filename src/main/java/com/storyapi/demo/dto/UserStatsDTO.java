package com.storyapi.demo.dto;

import com.storyapi.demo.Entity.UserDirectory.User;

public class UserStatsDTO {
    private long totalUsers;
    private long readers;
    private long authors;
    private long admins;
    private long pendingApplications;
    
    public UserStatsDTO(){}
    
    public UserStatsDTO(long totalUsers, long readers, long authors, long admins, long pendingApplications) {
        this.totalUsers = totalUsers;
        this.readers = readers;
        this.authors = authors;
        this.admins = admins;
        this.pendingApplications = pendingApplications;
    }
    
    public long getAdmins() {
        return admins;
    }
    
    public long getAuthors() {
        return authors;
    }
    
    public long getPendingApplications() {
        return pendingApplications;
    }
    public long getReaders() {
        return readers;
    }
    
    public long getTotalUsers() {
        return totalUsers;
    }
    
    public void setAdmins(long admins) {
        this.admins = admins;
    }
    
    public void setAuthors(long authors) {
        this.authors = authors;
    }
    
    public void setPendingApplications(long pendingApplications) {
        this.pendingApplications = pendingApplications;
    }
    
    public void setReaders(long readers) {
        this.readers = readers;
    }
    
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
}
